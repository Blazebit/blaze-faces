/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.commandbutton;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.blazebit.blazefaces.renderkit.CommandRenderer;
import com.blazebit.blazefaces.util.HTML5;
import com.blazebit.blazefaces.util.RendererUtils;

public class CommandButtonRenderer extends CommandRenderer {
    
    
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        String type = getType(context, component);

        writer.startElement("input", component);
        RendererUtils.encodeAttribute(writer, "id", clientId, null);
        RendererUtils.encodeAttribute(writer, "name", clientId, null);
        RendererUtils.encodeAttribute(writer, "class", component.getAttributes().get("styleClass"), null);
        RendererUtils.encodeAttribute(writer, "type", type, null);
        RendererUtils.encodeAttribute(writer, "value", component.getAttributes().get("value"), null);
        renderPassThruAttributes(context, component, HTML5.COMMON_ATTRIBUTES);
        writer.endElement("input");
        
        if("submit".equals(type))
            encodeBehaviors(context, (ClientBehaviorHolder)component);
    }
}
