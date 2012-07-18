/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.panelgroup;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.blazebit.blazefaces.renderkit.CoreRenderer;
import com.blazebit.blazefaces.util.HTML5;
import com.blazebit.blazefaces.util.RendererUtil;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.ResponseWriter;

public class PanelGroupRenderer extends CoreRenderer {

    private static final String DEFAULT_RENDER_TAG = "span";
    private static final String[] ALLOWED_TYPES = {
        "div",
        "span",
        "article",
        "aside",
        "footer",
        "header",
        "hgroup",
        "nav",
        "section",
        "h1",
        "h2",
        "h3",
        "h4",
        "h5",
        "h6",
        "address",
        "blockquote",
        "p",
        "code",
        "figure",
        "figcaption",
        "summary"
    };
    
    @Override
    public void encodeBegin(FacesContext ctx, UIComponent component) throws IOException {
        String htmlTag = getHtmlTag(ctx, component);
        
        if(htmlTag != null){
            ResponseWriter writer = ctx.getResponseWriter();
            writer.startElement(htmlTag, component);
            RendererUtil.encodeAttribute(writer, "id", component.getClientId(ctx), null);
            RendererUtil.encodeAttribute(writer, "class", component.getAttributes().get("styleClass"), null);
            renderPassThruAttributes(ctx, component, HTML5.COMMON_ATTRIBUTES);
            renderDataMapAttributes(ctx, component);
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        String htmlTag = getHtmlTag(context, component);
        
        if(htmlTag != null){
            ResponseWriter writer = context.getResponseWriter();
            writer.endElement(htmlTag);
            encodeBehaviors(context, (ClientBehaviorHolder) component);
        }
    }

    protected String getHtmlTag(FacesContext ctx, UIComponent p) {
        String tag = (String) p.getAttributes().get("type");
        
        for(String type : ALLOWED_TYPES){
            if(type.equalsIgnoreCase(tag))
                return type;
        }
        
        // Iterate over all attributes that could be set,
        // if none are set on the component, dont render the surrounding tag
        for(String attr : HTML5.COMMON_ATTRIBUTES){
            if(p.getAttributes().get(attr) != null)
                return DEFAULT_RENDER_TAG;
        }
        
        if(p.getAttributes().get("styleClass") != null)
            return DEFAULT_RENDER_TAG;
        
        if(!((ClientBehaviorHolder)p).getClientBehaviors().isEmpty())
            return DEFAULT_RENDER_TAG;
        
        return null;
    }
}
