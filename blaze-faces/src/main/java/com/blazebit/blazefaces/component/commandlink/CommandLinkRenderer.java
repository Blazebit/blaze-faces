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
package com.blazebit.blazefaces.component.commandlink;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import com.blazebit.blazefaces.renderkit.CoreRenderer;
import com.blazebit.blazefaces.util.ComponentUtils;
import com.blazebit.blazefaces.util.HTML;
import com.blazebit.blazefaces.util.RendererUtils;

public class CommandLinkRenderer extends CoreRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        CommandLink link = (CommandLink) component;
        if (link.isDisabled()) {
            return;
        }

        String param = component.getClientId();

        if (context.getExternalContext().getRequestParameterMap().containsKey(param)) {
            component.queueEvent(new ActionEvent(component));
        }
    }
    
    // Old implementation
    // TODO: merge code
//    private static final String STYLE_CLASS = "";
//    private static final String DISABLED_STYLE_CLASS = "";
//
//    @Override
//    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
//        ResponseWriter writer = context.getResponseWriter();
//        CommandLink comp = (CommandLink) component;
//        String clientId = comp.getClientId(context);
//        Object value = comp.getAttributes().get("value");
//        String styleClass = comp.getStyleClass();
//        UIComponent form = ComponentUtils.findParentForm(context, comp);
//        
//        if (form == null) {
//            throw new FacesException("Commandlink \"" + clientId + "\" must be inside a form component");
//        }
//
//        if (!comp.isDisabled()) {
//            String formClientId = form.getClientId(context);
//            styleClass = styleClass == null ? STYLE_CLASS : STYLE_CLASS + " " + styleClass;
//
//            writer.startElement("a", comp);
//            writer.writeAttribute("id", clientId, null);
//            writer.writeAttribute("href", "javascript:void(0);", null);
//            writer.writeAttribute("class", styleClass, null);
//            
//            if(!containsAjaxBehavior(context, comp))
//                encodeEventHandler(context, comp, formClientId, clientId);
//            encodeBehaviors(context, comp);
//            renderPassThruAttributes(context, comp, HTML5.COMMON_ATTRIBUTES);
//
//            if (value != null) {
//                writer.writeText(value, null);
//            } else {
//                renderChildren(context, comp);
//            }
//
//            writer.endElement("a");
//        } else {
//            styleClass = styleClass == null ? DISABLED_STYLE_CLASS : DISABLED_STYLE_CLASS + " " + styleClass;
//
//            writer.startElement("span", comp);
//            writer.writeAttribute("id", clientId, null);
//            writer.writeAttribute("class", styleClass, null);
//
//            renderPassThruAttributes(context, comp, HTML5.COMMON_TAG_ATTRIBUTES);
//
//            if (value != null) {
//                writer.writeText(value, null);
//            } else {
//                renderChildren(context, comp);
//            }
//
//            writer.endElement("span");
//        }
//    }
//    
//    protected void encodeEventHandler(FacesContext facesContext, UIComponent component, String formId, String decodeParam) {
//        StringBuilder request = new StringBuilder();
//        String type = (String) component.getAttributes().get("type");
//        request.append("BlazeJS.Util");
//
//        if("submit".equals(type)){
//            request.append(addSubmitParam(formId, decodeParam, decodeParam));
//
//            for (UIComponent child : component.getChildren()) {
//                if (child instanceof UIParameter) {
//                    UIParameter param = (UIParameter) child;
//
//                    request.append(addSubmitParam(formId, param.getName(), String.valueOf(param.getValue())));
//                }
//            }
//
//            request.append(".submit('").append(formId).append("');");
//        }else if("reset".equals(type)){
//            request.append(".reset('").append(formId).append("');");
//        }
//        
//        String script = RendererUtils.getEventHandlerScript(facesContext, component.getClientId(facesContext), "click", request.toString());
//        RendererUtils.addBodyBottomScript(facesContext, script);
//    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        CommandLink link = (CommandLink) component;
        String clientId = link.getClientId(context);
        Object label = link.getValue();

        if (!link.isDisabled()) {
            String request;
            String styleClass = link.getStyleClass();
            styleClass = styleClass == null ? CommandLink.STYLE_CLASS : CommandLink.STYLE_CLASS + " " + styleClass;

            writer.startElement("a", link);
            writer.writeAttribute("id", clientId, "id");
            writer.writeAttribute("href", "#", null);
            writer.writeAttribute("class", styleClass, null);

            if (link.isAjax()) {
                request = buildAjaxRequest(context, link, null);
            } else {
                UIComponent form = ComponentUtils.findParentForm(context, link);
                if (form == null) {
                    throw new FacesException("Commandlink \"" + clientId + "\" must be inside a form component");
                }

                request = buildNonAjaxRequest(context, link, form, clientId, true);
            }

            String onclick = link.getOnclick() != null ? link.getOnclick() + ";" + request : request;
        	RendererUtils.encodeSequentialEventHandler(context, clientId, "click", onclick.toString());
            //writer.writeAttribute("onclick", onclick, "onclick");

            renderPassThruAttributes(context, link, HTML.LINK_ATTRS, HTML.CLICK_EVENT);

            if (label != null) {
                writer.writeText(label, "value");
            } else {
                renderChildren(context, link);
            }

            writer.endElement("a");
        } else {
            String styleClass = link.getStyleClass();
            styleClass = styleClass == null ? CommandLink.DISABLED_STYLE_CLASS : CommandLink.DISABLED_STYLE_CLASS + " " + styleClass;

            writer.startElement("span", link);
            writer.writeAttribute("id", clientId, "id");
            writer.writeAttribute("class", styleClass, "styleclass");

            if (link.getStyle() != null) {
                writer.writeAttribute("style", link.getStyle(), "style");
            }

            if (label != null) {
                writer.writeText(label, "value");
            } else {
                renderChildren(context, link);
            }

            writer.endElement("span");
        }
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        //Do Nothing
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}