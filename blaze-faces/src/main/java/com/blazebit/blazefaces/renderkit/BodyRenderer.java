/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.renderkit;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.blazebit.blazefaces.util.RendererUtil;

public class BodyRenderer extends CoreRenderer {

    private static final String[] attrs = {
        "dir",
        "lang",
        "onclick",
        "ondblclick",
        "onkeydown",
        "onkeypress",
        "onkeyup",
        "onload",
        "onmousedown",
        "onmousemove",
        "onmouseout",
        "onmouseover",
        "onmouseup",
        "onunload",
        "style",
        "title",
        "xmlns"
    };
    
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String styleClass = (String) component.getAttributes().get("styleClass");
        writer.startElement("body", component);
        
        if (styleClass != null && styleClass.length() != 0) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        
        renderPassThruAttributes(context, component, attrs);
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        //no-op
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        UIViewRoot viewRoot = context.getViewRoot();
        Iterator<UIComponent> iter = (viewRoot.getComponentResources(context, "body")).listIterator();
        
        while (iter.hasNext()) {
            iter.next().encodeAll(context);
        }
        
        List<String> scripts = RendererUtil.getBodyBottomScripts(context);
        
        if(scripts != null && scripts.size() > 0){
            writer.startElement("script", null);
            writer.writeAttribute("type", "text/javascript", null);
            
            for(String script : scripts){
                writer.write(script);
            }
            
            writer.endElement("script");
        }
        
        RendererUtil.renderUnhandledMessages(context);
        writer.endElement("body");
    }
}
