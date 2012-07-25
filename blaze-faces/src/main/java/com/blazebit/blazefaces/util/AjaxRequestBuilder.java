/*
 * Copyright 2011-2012 Blazebit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blazebit.blazefaces.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;

/**
 * Helper to generate javascript code of an ajax call
 *
 * @author Christian Beikov
 */
public class AjaxRequestBuilder {

    protected StringBuilder sb;
    private boolean preventDefault = false;

    public AjaxRequestBuilder() {
        sb = new StringBuilder();
        sb.append("BlazeJS.Ajax.send({");
    }

    public AjaxRequestBuilder source(FacesContext context, UIComponent component, String source) {
        if (source != null) {
            sb.append("source:").append("'").append(source).append("'");
        } else {
            sb.append("source:").append("'").append(component.getClientId(context)).append("'");
        }

        return this;
    }

    public AjaxRequestBuilder form(String form) {
        if (form != null) {
            sb.append(",formId:'").append(form).append("'");
        }

        return this;
    }

    private String[] parseIds(String ids) {
        Pattern p = Pattern.compile("@\\(.+\\)\\s*");
        Matcher m = p.matcher(ids);
        String selector, regular;

        if (m.find()) {
            selector = m.group().trim();
            regular = m.replaceAll("");
        } else {
            selector = null;
            regular = ids;
        }

        if (isValueBlank(regular)) {
            regular = null;
        }

        return new String[]{regular, selector};
    }

    private boolean isValueBlank(String value) {
        if (value == null) {
            return true;
        }

        return value.trim().equals("");
    }

    public AjaxRequestBuilder process(FacesContext context, UIComponent component, String ids) {
        addIds(context, component, ids, "process", "processSelector");

        return this;
    }

    public AjaxRequestBuilder update(FacesContext context, UIComponent component, String ids) {
        addIds(context, component, ids, "update", "updateSelector");

        return this;
    }

    private AjaxRequestBuilder addIds(FacesContext context, UIComponent component, String ids, String key, String keySel) {
        if (!isValueBlank(ids)) {
            String[] parsed = parseIds(ids);
            String regular = parsed[0];
            String selector = parsed[1];

            if (regular != null) {
                sb.append(",").append(key).append(":'").append(ComponentUtils.findClientIds(context, component, regular)).append("'");
            }

            if (selector != null) {
                sb.append(",").append(keySel).append(":'").append(selector).append("'");
            }
        }

        return this;
    }

    public AjaxRequestBuilder event(String event) {
        sb.append(",event:'").append(event).append("'");

        return this;
    }

    public AjaxRequestBuilder async(boolean async) {
        if (async) {
            sb.append(",async:true");
        }

        return this;
    }

    public AjaxRequestBuilder global(boolean global) {
        if (!global) {
            sb.append(",global:false");
        }

        return this;
    }

    public AjaxRequestBuilder partialSubmit(boolean value, boolean partialSubmitSet) {
        //component can override global setting
        boolean partialSubmit = partialSubmitSet ? value : ComponentUtils.isPartialSubmitEnabled(FacesContext.getCurrentInstance());

        if (partialSubmit) {
            sb.append(",partialSubmit:true");
        }

        return this;
    }

    public AjaxRequestBuilder onstart(String onstart) {
        if (onstart != null) {
            sb.append(",onstart:function(cfg){").append(onstart).append(";}");
        }

        return this;
    }

    public AjaxRequestBuilder onerror(String onerror) {
        if (onerror != null) {
            sb.append(",onerror:function(xhr,status,error){").append(onerror).append(";}");
        }

        return this;
    }

    public AjaxRequestBuilder onsuccess(String onsuccess) {
        if (onsuccess != null) {
            sb.append(",onsuccess:function(data,status,xhr){").append(onsuccess).append(";}");
        }

        return this;
    }

    public AjaxRequestBuilder oncomplete(String oncomplete) {
        if (oncomplete != null) {
            sb.append(",oncomplete:function(xhr,status,args){").append(oncomplete).append(";}");
        }

        return this;
    }

    public AjaxRequestBuilder params(UIComponent component) {
        boolean paramWritten = false;

        for (UIComponent child : component.getChildren()) {
            if (child instanceof UIParameter) {
                UIParameter parameter = (UIParameter) child;

                if (!paramWritten) {
                    paramWritten = true;
                    sb.append(",params:[");
                } else {
                    sb.append(",");
                }

                sb.append("{name:").append("'").append(parameter.getName()).append("',value:'").append(parameter.getValue()).append("'}");
            }
        }

        if (paramWritten) {
            sb.append("]");
        }

        return this;
    }

    public AjaxRequestBuilder passParams() {
        sb.append(",params:arguments[0]");

        return this;
    }

    public AjaxRequestBuilder preventDefault() {
        this.preventDefault = true;

        return this;
    }

    public StringBuilder getBuffer() {
        return sb;
    }

    public String build() {
        sb.append("});");

        if (preventDefault) {
            sb.append("event.preventDefault();");
        }

        String request = sb.toString();
        sb.setLength(0);

        return request;
    }

    public String buildBehavior() {
        sb.append("}, arguments[1]);");

        if (preventDefault) {
            sb.append("event.preventDefault();");
        }

        String request = sb.toString();
        sb.setLength(0);

        return request;
    }
}
