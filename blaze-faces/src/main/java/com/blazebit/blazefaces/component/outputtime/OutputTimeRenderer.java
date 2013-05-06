/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.component.outputtime;

import com.blazebit.blazefaces.apt.JsfRenderer;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.DateTimeConverter;

import com.blazebit.blazefaces.renderkit.OutputRenderer;
import com.blazebit.blazefaces.util.HTML5;
import com.blazebit.blazefaces.util.RendererUtils;
import com.ocpsoft.pretty.time.PrettyTime;

@JsfRenderer
public class OutputTimeRenderer extends OutputRenderer {
    
    public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    @Override
    public void encodeBegin(FacesContext ctx, UIComponent component) throws IOException {
        OutputTime p = (OutputTime) component;
        ResponseWriter writer = ctx.getResponseWriter();
        writer.startElement("time", component);
        // removed the following line from component constructor
//        setConverter(getFacesContext().getApplication().createConverter(DateTimeConverter.class));
        initConverter((DateTimeConverter) p.getConverter(), p);

        RendererUtils.encodeAttribute(writer, "id", p.getClientId(ctx), null);
        RendererUtils.encodeAttribute(writer, "class", p.getAttributes().get("styleClass"), null);
        RendererUtils.encodeAttribute(writer, "pubdate", p.getAttributes().get("pubdate"), null);
        
        Object val = ((ValueHolder) component).getValue();
        
        if(val != null){
            RendererUtils.encodeAttribute(writer, "datetime", df.format((Date) val), null);
        }
        
        renderPassThruAttributes(ctx, component, HTML5.COMMON_ATTRIBUTES);
        renderDataMapAttributes(ctx, component);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
    
    @Override
    public void encodeChildren(FacesContext ctx, UIComponent component) throws IOException {
        if (component.getChildCount() > 0) {
            renderChildren(ctx, component);
        } else {
            ResponseWriter writer = ctx.getResponseWriter();
            
            if(component.getAttributes().get("pretty") != null && "true".equals(component.getAttributes().get("pretty"))){
                writer.writeText(getPretty(ctx, component, (Date)((ValueHolder) component).getValue()), component, "value");
            }else{
                writer.writeText(getCurrentValue(ctx, component), component, "value");
            }
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("time");
        encodeBehaviors(context, (ClientBehaviorHolder) component);
    }

    private void initConverter(DateTimeConverter converter, OutputTime p) {
        if (p.getAttributes().get("type") != null) {
            converter.setType((String) p.getAttributes().get("type"));
        }
        if (p.getAttributes().get("dateStyle") != null) {
            converter.setDateStyle((String) p.getAttributes().get("dateStyle"));
        }
        if (p.getAttributes().get("timeStyle") != null) {
            converter.setTimeStyle((String) p.getAttributes().get("timeStyle"));
        }
        if (p.getAttributes().get("pattern") != null) {
            converter.setPattern((String) p.getAttributes().get("pattern"));
        }

        if (p.getAttributes().get("timeZone") != null) {
            Object timeZone = p.getAttributes().get("timeZone");

            if (timeZone instanceof TimeZone) {
                converter.setTimeZone((TimeZone) timeZone);
            } else {
                converter.setTimeZone(TimeZone.getTimeZone((String) timeZone));
            }
        }

        if (p.getAttributes().get("locale") != null) {
            converter.setLocale((Locale) p.getAttributes().get("locale"));
        }
    }
    
    private String getPretty(FacesContext ctx, UIComponent component, Date date){
        Locale l = null;
        
        if (component.getAttributes().get("locale") != null) {
            l = (Locale) component.getAttributes().get("locale");
        }
        
        if(l == null){
            l = ctx.getViewRoot().getLocale();
        }
        
        PrettyTime pt = new PrettyTime(l);
        return pt.format(date);
    }
}
