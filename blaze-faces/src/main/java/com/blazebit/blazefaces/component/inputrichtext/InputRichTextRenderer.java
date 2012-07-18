/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.inputrichtext;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.blazebit.blazefaces.renderkit.InputRenderer;
import com.blazebit.blazefaces.util.HTML5;
import com.blazebit.blazefaces.util.RendererUtil;

public class InputRichTextRenderer extends InputRenderer {
    
    @Override
    public void decode(FacesContext ctx, UIComponent component) {
        
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        
        writer.startElement("textarea", component);
        RendererUtil.encodeAttribute(writer, "id", clientId, null);
        RendererUtil.encodeAttribute(writer, "name", clientId, null);
        RendererUtil.encodeAttribute(writer, "class", component.getAttributes().get("styleClass"), null);
        renderPassThruAttributes(context, component, HTML5.COMMON_ATTRIBUTES);
        encodeBehaviors(context, (ClientBehaviorHolder) component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String value = getCurrentValue(context, component);
        
        if(value != null){
            writer.writeText(value, component, "value");
        }
        
        writer.endElement("textarea");
    }
    
    
}
