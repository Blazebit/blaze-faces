/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.component.inputfile;

import com.blazebit.blazefaces.apt.JsfRenderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletRequestWrapper;

import org.apache.commons.fileupload.FileItem;

import com.blazebit.blazefaces.model.DefaultUploadedFile;
import com.blazebit.blazefaces.renderkit.OutputRenderer;
import com.blazebit.blazefaces.util.HTML5;
import com.blazebit.blazefaces.util.RendererUtils;
import com.blazebit.blazefaces.webapp.MultipartRequest;

@JsfRenderer
public class InputFileRenderer extends OutputRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        InputFile inputFile = (InputFile) component;
        String clientId = inputFile.getClientId(context);
        MultipartRequest multipartRequest = getMultiPartRequestInChain(context);
        
        if(multipartRequest != null) {
            FileItem file = multipartRequest.getFileItem(clientId);

            if(file.getName().equals("")) {
                inputFile.setSubmittedValue("");
            } else {
                inputFile.setSubmittedValue(new DefaultUploadedFile(file));
            }
        }
    }
    
    /**
     * Finds our MultipartRequestServletWrapper in case application contains other RequestWrappers
     */
    private MultipartRequest getMultiPartRequestInChain(FacesContext facesContext) {
        Object request = facesContext.getExternalContext().getRequest();
        
        while(request instanceof ServletRequestWrapper) {
            if(request instanceof MultipartRequest) {
                return (MultipartRequest) request;
            }
            else {
                request = ((ServletRequestWrapper) request).getRequest();
            }
        }
        
        return null;
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
