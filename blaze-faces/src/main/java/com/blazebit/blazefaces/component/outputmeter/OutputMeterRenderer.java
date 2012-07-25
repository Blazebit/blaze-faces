/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.outputmeter;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.blazebit.blazefaces.renderkit.OutputRenderer;
import com.blazebit.blazefaces.util.HTML5;
import com.blazebit.blazefaces.util.RendererUtils;

public class OutputMeterRenderer extends OutputRenderer {
    
    @Override
    public void encodeBegin(FacesContext ctx, UIComponent component) throws IOException {
        OutputMeter p = (OutputMeter) component;
        ResponseWriter writer = ctx.getResponseWriter();
        writer.startElement("meter", component);

        RendererUtils.encodeAttribute(writer, "id", p.getClientId(ctx), null);
        RendererUtils.encodeAttribute(writer, "class", p.getAttributes().get("styleClass"), null);
        renderPassThruAttributes(ctx, component, HTML5.COMMON_ATTRIBUTES);
        renderPassThruAttributes(ctx, component, HTML5.METER_ELEMENT_ATTRIBUTES);
        renderDataMapAttributes(ctx, component);
    }

    @Override
    public void encodeChildren(FacesContext ctx, UIComponent component) throws IOException {
        if (component.getChildCount() > 0) {
            renderChildren(ctx, component);
        } else {
            ResponseWriter writer = ctx.getResponseWriter();
            writer.writeText(getCurrentValue(ctx, component), component, "value");
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("meter");
        encodeBehaviors(context, (ClientBehaviorHolder) component);
    }
}
