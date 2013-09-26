/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.component.inputfile;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.blazebit.blazefaces.apt.JsfRenderer;
import com.blazebit.blazefaces.renderkit.OutputRenderer;
import com.blazebit.blazefaces.util.HTML5;
import com.blazebit.blazefaces.util.RendererUtils;

@JsfRenderer
public class InputFileRenderer extends OutputRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        InputFile inputFile = (InputFile) component;
        String clientId = inputFile.getClientId(context);
        
        Part part = null;
        
        try {
            part = ((HttpServletRequest)context.getExternalContext().getRequest()).getPart(clientId);
        } catch (IOException ex) {
            throw new FacesException("Could not get the part", ex);
        } catch (ServletException ex) {
            throw new FacesException("Could not get the part", ex);
        }
        
        if(part != null) {
            if(part.getName().equals("")) {
                inputFile.setSubmittedValue("");
            } else {
                inputFile.setSubmittedValue(part);
            }
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        InputFile inputFile = (InputFile) component;
        
        encodeMarkup(context, inputFile);
//        encodeScript(context, fileUpload);
    }
    
    public void encodeMarkup(FacesContext context, InputFile component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("input", component);

        RendererUtils.encodeAttribute(writer, "id", component.getClientId(context), null);
        RendererUtils.encodeAttribute(writer, "name", component.getClientId(context), null);
        RendererUtils.encodeAttribute(writer, "type", "file", null);
        RendererUtils.encodeAttribute(writer, "class", component.getStyleClass(), null);
        renderPassThruAttributes(context, component, HTML5.COMMON_ATTRIBUTES);
        renderPassThruAttributes(context, component, HTML5.COMMON_TAG_ATTRIBUTES);
        renderDataMapAttributes(context, component);
        writer.endElement("input");
    }
}
