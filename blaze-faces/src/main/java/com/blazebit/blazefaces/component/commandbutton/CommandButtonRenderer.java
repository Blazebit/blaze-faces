/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.commandbutton;

import com.blazebit.blazefaces.renderkit.CommandRenderer;
import com.blazebit.blazefaces.util.HTML5;
import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.blazebit.blazefaces.util.RendererUtil;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.ResponseWriter;

public class CommandButtonRenderer extends CommandRenderer {
    
    
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        String type = getType(context, component);

        writer.startElement("input", component);
        RendererUtil.encodeAttribute(writer, "id", clientId, null);
        RendererUtil.encodeAttribute(writer, "name", clientId, null);
        RendererUtil.encodeAttribute(writer, "class", component.getAttributes().get("styleClass"), null);
        RendererUtil.encodeAttribute(writer, "type", type, null);
        RendererUtil.encodeAttribute(writer, "value", component.getAttributes().get("value"), null);
        renderPassThruAttributes(context, component, HTML5.COMMON_ATTRIBUTES);
        writer.endElement("input");
        
        if("submit".equals(type))
            encodeBehaviors(context, (ClientBehaviorHolder)component);
    }
}
