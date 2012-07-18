/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.button;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.blazebit.blazefaces.renderkit.OutputRenderer;
import com.blazebit.blazefaces.util.HTML5;
import com.blazebit.blazefaces.util.RendererUtil;

public class ButtonRenderer extends OutputRenderer {
    
    private static final String DEFAULT_TYPE = "button";
    private static final String[] ALLOWED_TYPES = {
        "button",
        "reset"
    };
    
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Button comp = (Button) component;
        String clientId = comp.getClientId(context);
        String type = getType(context, component);
        String value = getCurrentValue(context, component);

        writer.startElement("button", comp);
        RendererUtil.encodeAttribute(writer, "id", clientId, null);
        RendererUtil.encodeAttribute(writer, "name", clientId, null);
        RendererUtil.encodeAttribute(writer, "class", comp.getAttributes().get("styleClass"), null);
        RendererUtil.encodeAttribute(writer, "type", type, null);
        RendererUtil.encodeAttribute(writer, "value", value, null);
        renderPassThruAttributes(context, component, HTML5.COMMON_ATTRIBUTES);
        
        if(component.getChildCount() > 0){
            renderChildren(context, component);
        }else{
            writer.writeText(value, null);
        }
        
        writer.endElement("button");
        
        if("button".equals(type))
            encodeBehaviors(context, comp);
    }

    protected String getType(FacesContext context, UIComponent component) {
        String inputType = (String) component.getAttributes().get("type");
        
        for(String type : ALLOWED_TYPES){
            if(type.equalsIgnoreCase(inputType))
                return type;
        }
        
        return DEFAULT_TYPE;
    }
}
