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
package com.blazebit.blazefaces.component.commandbutton;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import com.blazebit.blazefaces.renderkit.CommandRenderer;
import com.blazebit.blazefaces.renderkit.CoreRenderer;
import com.blazebit.blazefaces.util.ArrayUtils;
import com.blazebit.blazefaces.util.ComponentUtils;
import com.blazebit.blazefaces.util.HTML;
import com.blazebit.blazefaces.util.RendererUtils;

public class CommandButtonRenderer extends CommandRenderer {
	
	private static final String[] ALLOWED_TYPES = ArrayUtils.concat(CommandRenderer.ALLOWED_TYPES, new String[]{"button"});
	
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        CommandButton button = (CommandButton) component;

        encodeMarkup(context, button);
        encodeScript(context, button);
    }
    
    @Override
    protected String[] getAllowedTypes(){
    	return ALLOWED_TYPES;
    }
    
    // Old implementation
    // TODO: merge code
//    @Override
//    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
//        ResponseWriter writer = context.getResponseWriter();
//        String clientId = component.getClientId(context);
//        String type = getType(context, component);
//
//        writer.startElement("input", component);
//        RendererUtils.encodeAttribute(writer, "id", clientId, null);
//        RendererUtils.encodeAttribute(writer, "name", clientId, null);
//        RendererUtils.encodeAttribute(writer, "class", component.getAttributes().get("styleClass"), null);
//        RendererUtils.encodeAttribute(writer, "type", type, null);
//        RendererUtils.encodeAttribute(writer, "value", component.getAttributes().get("value"), null);
//        renderPassThruAttributes(context, component, HTML5.COMMON_ATTRIBUTES);
//        writer.endElement("input");
//        
//        if("submit".equals(type))
//            encodeBehaviors(context, (ClientBehaviorHolder)component);
//    }
    
    protected void encodeMarkup(FacesContext context, CommandButton button) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = button.getClientId(context);
        String type = getType(context, button.getType());
        String value = (String) button.getValue();
        String icon = button.getIcon();

        StringBuilder onclick = new StringBuilder();
        
        if (button.getOnclick() != null) {
            onclick.append(button.getOnclick()).append(";");
        }

        writer.startElement("button", button);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("name", clientId, "name");
        writer.writeAttribute("class", button.resolveStyleClass(), "styleClass");

        if ("submit".equals(type)) {
            String request;

            if (button.isAjax()) {
                request = buildAjaxRequest(context, button, null);
            } else {
                UIComponent form = ComponentUtils.findParentForm(context, button);
                
                if (form == null) {
                    throw new FacesException("CommandButton : \"" + clientId + "\" must be inside a form element");
                }

                request = buildNonAjaxRequest(context, button, form, null, false);
            }

            onclick.append(request);
        }

        String onclickBehaviors = getOnclickBehaviors(context, button);
        
        if (onclickBehaviors != null) {
            onclick.append(onclickBehaviors).append(";");
        }

        if (onclick.length() > 0) {
        	RendererUtils.encodeSequentialEventHandler(context, clientId, "click", onclick.toString());
        }

        renderPassThruAttributes(context, button, HTML.BUTTON_ATTRS, HTML.CLICK_EVENT);

        if (button.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", "disabled");
        }
        if (button.isReadonly()) {
            writer.writeAttribute("readonly", "readonly", "readonly");
        }

        //icon
        if (icon != null) {
            String defaultIconClass = button.getIconPos().equals("left") ? HTML.BUTTON_LEFT_ICON_CLASS : HTML.BUTTON_RIGHT_ICON_CLASS;
            String iconClass = defaultIconClass + " " + icon;

            writer.startElement("span", null);
            writer.writeAttribute("class", iconClass, null);
            writer.endElement("span");
        }

        //text
        writer.startElement("span", null);
        writer.writeAttribute("class", HTML.BUTTON_TEXT_CLASS, null);

        if (value == null) {
            writer.write("ui-button");
        } else {
            if (button.isEscape()) {
                writer.writeText(value, "value");
            } else {
                writer.write(value);
            }
        }

        writer.endElement("span");

        writer.endElement("button");
    }

    protected void encodeScript(FacesContext context, CommandButton button) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = button.getClientId(context);

        startScript(writer, clientId);

        writer.write("BlazeFaces.cw('CommandButton','" + button.resolveWidgetVar() + "',{");
        writer.write("id:'" + clientId + "'");
        writer.write("});");

        endScript(writer);
    }
}