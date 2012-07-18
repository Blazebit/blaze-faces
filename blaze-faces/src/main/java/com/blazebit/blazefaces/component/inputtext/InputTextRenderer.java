/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.inputtext;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.blazebit.blazefaces.renderkit.InputRenderer;
import com.blazebit.blazefaces.util.HTML5;
import com.blazebit.blazefaces.util.RendererUtil;

public class InputTextRenderer extends InputRenderer {
    private static final String DEFAULT_TYPE = "text";
    private static final String[] ALLOWED_TYPES = {
        "text",
        "password",
        "hidden"
    };
    
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        String value = getCurrentValue(context, component);
        String type = getType(context, component);
        
        writer.startElement("input", component);
        RendererUtil.encodeAttribute(writer, "id", clientId, null);
        RendererUtil.encodeAttribute(writer, "name", clientId, null);
        RendererUtil.encodeAttribute(writer, "type", type, null);
        RendererUtil.encodeAttribute(writer, "class", component.getAttributes().get("styleClass"), null);
        RendererUtil.encodeAttribute(writer, "value", value, null);
        renderPassThruAttributes(context, component, HTML5.COMMON_ATTRIBUTES);
        writer.endElement("input");
        encodeBehaviors(context, (ClientBehaviorHolder) component);
    }

    private String getType(FacesContext context, UIComponent component) {
        String inputType = (String) component.getAttributes().get("type");
        
        for(String type : ALLOWED_TYPES){
            if(type.equalsIgnoreCase(inputType))
                return type;
        }
        
        return DEFAULT_TYPE;
    }
    
    
}
