/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.renderkit.encoder;

import com.blazebit.blazefaces.context.BlazeEncoder;
import java.io.IOException;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author Christian Beikov
 */
public class PanelEncoder implements BlazeEncoder{

    public void encode(FacesContext ctx, Map<String, Object> attributes) throws IOException {
        ResponseWriter response = ctx.getResponseWriter();
        response.startElement("div", null);
        response.writeAttribute("id", attributes.get("id"), null);
        response.writeAttribute("class", "panel-wrapper media-uploading", null);
        
            response.startElement("div", null);
            response.writeAttribute("class", "panel-title", null);
                response.startElement("div", null);
                response.writeAttribute("class", "lft", null);
                response.endElement("div");
                
                response.startElement("div", null);
                response.writeAttribute("class", "mdl", null);
                    response.startElement("a", null);
                    response.writeAttribute("class", "toggle", null);
                    response.writeAttribute("href", "#", null);
                    response.write("Toggle");
                    response.endElement("a");
                    
                    response.startElement("h3", null);
                    response.write(attributes.get("title") != null ? attributes.get("title").toString() : "");
                    response.endElement("h3");
                    
                    response.startElement("span", null);
                    response.writeAttribute("class", "sortable-click-area", null);
                    response.write("Move");
                    response.endElement("span");
                response.endElement("div");
                
                response.startElement("div", null);
                response.writeAttribute("class", "rgt", null);
                response.endElement("div");
            response.endElement("div");
            
            response.startElement("div", null);
            response.writeAttribute("class", "panel-body", null);
                response.startElement("div", null);
                response.writeAttribute("class", "panel-content", null);
                
                if(attributes.get("chain") != null)
                    ((BlazeEncoder)attributes.get("chain")).encode(ctx, attributes);
                
                response.endElement("div");
                
                response.startElement("div", null);
                response.writeAttribute("class", "panel-bottom", null);
                    response.startElement("div", null);
                    response.writeAttribute("class", "lft", null);
                    response.endElement("div");
                    response.startElement("div", null);
                    response.writeAttribute("class", "mdl", null);
                    response.endElement("div");
                    response.startElement("div", null);
                    response.writeAttribute("class", "rgt", null);
                    response.endElement("div");
                response.endElement("div");
            response.endElement("div");
            
        response.endElement("div");
    }
}
