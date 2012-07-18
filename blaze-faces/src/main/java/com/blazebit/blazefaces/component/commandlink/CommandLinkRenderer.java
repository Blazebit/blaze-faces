/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.commandlink;

import com.blazebit.blazefaces.renderkit.CommandRenderer;
import com.blazebit.blazefaces.util.ComponentUtil;
import com.blazebit.blazefaces.util.HTML5;
import com.blazebit.blazefaces.util.RendererUtil;
import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import javax.faces.FacesException;
import javax.faces.component.UIParameter;
import javax.faces.context.ResponseWriter;

public class CommandLinkRenderer extends CommandRenderer {
    
    private static final String STYLE_CLASS = "";
    private static final String DISABLED_STYLE_CLASS = "";

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        CommandLink comp = (CommandLink) component;
        String clientId = comp.getClientId(context);
        Object value = comp.getAttributes().get("value");
        String styleClass = comp.getStyleClass();
        UIComponent form = ComponentUtil.findParentForm(context, comp);
        
        if (form == null) {
            throw new FacesException("Commandlink \"" + clientId + "\" must be inside a form component");
        }

        if (!comp.isDisabled()) {
            String formClientId = form.getClientId(context);
            styleClass = styleClass == null ? STYLE_CLASS : STYLE_CLASS + " " + styleClass;

            writer.startElement("a", comp);
            writer.writeAttribute("id", clientId, null);
            writer.writeAttribute("href", "javascript:void(0);", null);
            writer.writeAttribute("class", styleClass, null);
            
            if(!containsAjaxBehavior(context, comp))
                encodeEventHandler(context, comp, formClientId, clientId);
            encodeBehaviors(context, comp);
            renderPassThruAttributes(context, comp, HTML5.COMMON_ATTRIBUTES);

            if (value != null) {
                writer.writeText(value, null);
            } else {
                renderChildren(context, comp);
            }

            writer.endElement("a");
        } else {
            styleClass = styleClass == null ? DISABLED_STYLE_CLASS : DISABLED_STYLE_CLASS + " " + styleClass;

            writer.startElement("span", comp);
            writer.writeAttribute("id", clientId, null);
            writer.writeAttribute("class", styleClass, null);

            renderPassThruAttributes(context, comp, HTML5.COMMON_TAG_ATTRIBUTES);

            if (value != null) {
                writer.writeText(value, null);
            } else {
                renderChildren(context, comp);
            }

            writer.endElement("span");
        }
    }
    
    protected void encodeEventHandler(FacesContext facesContext, UIComponent component, String formId, String decodeParam) {
        StringBuilder request = new StringBuilder();
        String type = (String) component.getAttributes().get("type");
        request.append("BlazeJS.Util");

        if("submit".equals(type)){
            request.append(addSubmitParam(formId, decodeParam, decodeParam));

            for (UIComponent child : component.getChildren()) {
                if (child instanceof UIParameter) {
                    UIParameter param = (UIParameter) child;

                    request.append(addSubmitParam(formId, param.getName(), String.valueOf(param.getValue())));
                }
            }

            request.append(".submit('").append(formId).append("');");
        }else if("reset".equals(type)){
            request.append(".reset('").append(formId).append("');");
        }
        
        String script = RendererUtil.getEventHandlerScript(facesContext, component.getClientId(facesContext), "click", request.toString());
        RendererUtil.addBodyBottomScript(facesContext, script);
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
