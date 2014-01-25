/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.component.inputfiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class InputFilesRenderer extends OutputRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        InputFiles inputFile = (InputFiles) component;
        
        if(!inputFile.isDisabled()) {
            String clientId = inputFile.getClientId(context);
            
            List<Part> parts = new ArrayList<Part>();
            
            try {
                for(Part p : ((HttpServletRequest)context.getExternalContext().getRequest()).getParts()) {
                    if(clientId.equals(p.getName())) {
                        parts.add(p);
                    }
                }
            } catch (IOException ex) {
                throw new FacesException("Could not get the parts", ex);
            } catch (ServletException ex) {
                throw new FacesException("Could not get the parts", ex);
            }
            
            inputFile.setSubmittedValue(parts.toArray(new Part[0]));
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        String clientId = component.getClientId(context);
        InputFiles inputFile = (InputFiles) component;
        
        encodeMarkup(context, inputFile, clientId);
        encodeScript(context, inputFile, clientId);
    }
    
    public void encodeMarkup(FacesContext context, InputFiles component, String clientId) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", "ui-fileupload", null);
            encodeComponentContainer(context, component, clientId);
            encodeFileContainer(context, component, clientId);
            encodeGalleryWidget(context, component, clientId);
            encodeUploadTemplate(context, component, clientId);
            encodeDownloadTemplate(context, component, clientId);
        writer.endElement("div");
    }
    
    public void encodeStartButton(FacesContext context, InputFiles component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("button", null);
        writer.writeAttribute("type", "submit", null);
        writer.writeAttribute("class", "ui-fileupload-upload", null);
        writer.writeText("Start upload", null);
        writer.endElement("button");
    }
    
    public void encodeResetButton(FacesContext context, InputFiles component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("button", null);
        writer.writeAttribute("type", "reset", null);
        writer.writeAttribute("class", "ui-fileupload-cancel", null);
        writer.writeText("Cancel upload", null);
        writer.endElement("button");
    }
    
    public void encodeDeleteButton(FacesContext context, InputFiles component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("button", null);
        writer.writeAttribute("type", "button", null);
        writer.writeAttribute("class", "ui-fileupload-delete", null);
        writer.writeText("Delete", null);
        writer.endElement("button");
    }
    
    public void encodeCheckbox(FacesContext context, InputFiles component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("input", null);
        writer.writeAttribute("type", "checkbox", null);
        writer.writeAttribute("class", "toggle", null);
        writer.endElement("input");
    }
    
    public void encodeComponentContainer(FacesContext context, InputFiles component, String clientId) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", null);
        writer.writeAttribute("class", "ui-fileupload-buttonbar", null);

            writer.startElement("div", null);
            writer.writeAttribute("class", "fileupload-buttons", null);
                writer.startElement("span", null);
                writer.writeAttribute("class", "ui-fileupload-choose", null);
                    writer.startElement("span", null);
                    writer.writeText("Add files...", null);
                    writer.endElement("span");
                    encodeInput(context, component, clientId);
                writer.endElement("span");
                
                encodeStartButton(context, component);
                encodeResetButton(context, component);
                encodeDeleteButton(context, component);
                encodeCheckbox(context, component);

//                writer.startElement("span", null);
//                writer.writeAttribute("class", "fileinput-button", null);
//                writer.endElement("span");
            
            writer.endElement("div");

            writer.startElement("div", null);
            writer.writeAttribute("class", "fileupload-progress fade", null);
            writer.writeAttribute("style", "display: none", null);

                writer.startElement("div", null);
                writer.writeAttribute("class", "progress", null);
                writer.writeAttribute("role", "progressbar", null);
                writer.writeAttribute("aria-valuemin", "0", null);
                writer.writeAttribute("aria-valuemax", "100", null);
                writer.endElement("div");
                
                writer.startElement("div", null);
                writer.writeAttribute("class", "progress-extended", null);
                writer.writeText("&nbsp;", null);
                writer.endElement("div");
            
            writer.endElement("div");
        writer.endElement("div");
    }
    
    public void encodeInput(FacesContext context, InputFiles component, String clientId) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("input", component);

        RendererUtils.encodeAttribute(writer, "id", clientId + "_input", null);
        RendererUtils.encodeAttribute(writer, "name", clientId + "_input", null);
        RendererUtils.encodeAttribute(writer, "type", "file", null);
//        RendererUtils.encodeAttribute(writer, "class", component.getStyleClass(), null, "styleClass");
        RendererUtils.encodeBooleanAttribute(writer, "disabled", component.isDisabled(), false, "disabled");
        RendererUtils.encodeBooleanAttribute(writer, "multiple", true, false, null);
        // Style-Attribute is included here
//        renderPassThruAttributes(context, component, HTML5.COMMON_ATTRIBUTES);
//        renderPassThruAttributes(context, component, HTML5.COMMON_TAG_ATTRIBUTES);
//        renderDataMapAttributes(context, component);
        writer.endElement("input");
    }
    
    public void encodeFileContainer(FacesContext context, InputFiles component, String clientId) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", null);
        writer.writeAttribute("class", "ui-fileupload-content", null);
            writer.startElement("table", null);
            writer.writeAttribute("class", "ui-fileupload-files", null);
            writer.writeAttribute("role", "presentation", null);
                writer.startElement("tbody", null);
                writer.writeAttribute("class", "files", null);
                writer.endElement("tbody");
            writer.endElement("table");
        writer.endElement("div");
    }
    
    public void encodeGalleryWidget(FacesContext context, InputFiles component, String clientId) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", null);
        writer.writeAttribute("id", "blueimp-gallery", null);
        writer.writeAttribute("class", "blueimp-gallery blueimp-gallery-controls", null);
        writer.writeAttribute("data-filters", ":even", null);
            writer.startElement("div", null);
            writer.writeAttribute("class", "sliders", null);
            writer.endElement("div");

            writer.startElement("h3", null);
            writer.writeAttribute("class", "title", null);
            writer.endElement("h3");

            writer.startElement("a", null);
            writer.writeAttribute("class", "prev", null);
            writer.writeText("‹", null);
            writer.endElement("a");

            writer.startElement("a", null);
            writer.writeAttribute("class", "next", null);
            writer.writeText("›", null);
            writer.endElement("a");

            writer.startElement("a", null);
            writer.writeAttribute("class", "close", null);
            writer.writeText("×", null);
            writer.endElement("a");

            writer.startElement("a", null);
            writer.writeAttribute("class", "play-pause", null);
            writer.endElement("a");

            writer.startElement("ol", null);
            writer.writeAttribute("class", "indicator", null);
            writer.endElement("ol");
        writer.endElement("div");
    }
    
    public void encodeUploadTemplate(FacesContext context, InputFiles component, String clientId) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("script", null);
        writer.writeAttribute("id", clientId + "_template_upload", null);
        writer.writeAttribute("type", "text/x-tmpl", null);
        writer.writeText("{% for (var i=0, file; file=o.files[i]; i++) { %}", null);
        
        if(component.getFacet("uploadTemplate") == null) {
            encodeDefaultUploadTemplate(context, component, clientId);
        }

        writer.writeText("{% } %}", null);
        writer.endElement("script");
    }
    
    public void encodeDownloadTemplate(FacesContext context, InputFiles component, String clientId) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("script", null);
        writer.writeAttribute("id", clientId + "_template_download", null);
        writer.writeAttribute("type", "text/x-tmpl", null);
        writer.writeText("{% for (var i=0, file; file=o.files[i]; i++) { %}", null);
        
        if(component.getFacet("downloadTemplate") == null) {
            encodeDefaultDownloadTemplate(context, component, clientId);
        }

        writer.writeText("{% } %}", null);
        writer.endElement("script");
    }
    
    public void encodeDefaultUploadTemplate(FacesContext context, InputFiles component, String clientId) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("tr", null);
        writer.writeAttribute("class", "template-upload fade", null);
            writer.startElement("td", null);
                writer.startElement("span", null);
                writer.writeAttribute("class", "preview", null);
                writer.endElement("span");
            writer.endElement("td");

            writer.startElement("td", null);
                writer.startElement("p", null);
                writer.writeAttribute("class", "name", null);
                writer.writeText("{%=file.name%}", null);
                writer.endElement("p");
                
                writer.writeText("{% if (file.error) { %}", null);
                    writer.startElement("div", null);
                        writer.startElement("span", null);
                        writer.writeAttribute("class", "error", null);
                        writer.writeText("Error:", null);
                        writer.endElement("span");
                        
                        writer.writeText("{%=file.error%}", null);
                    writer.endElement("div");
                writer.writeText("{% } %}", null);
            writer.endElement("td");

            writer.startElement("td", null);
                writer.startElement("p", null);
                writer.writeAttribute("class", "size", null);
                writer.writeText("{%=o.formatFileSize(file.size)%}", null);
                writer.endElement("p");
                
                writer.writeText("{% if (!o.file.error) { %}", null);
                    writer.startElement("div", null);
                    writer.writeAttribute("class", "progress", null);
                    writer.endElement("div");
                writer.writeText("{% } %}", null);
            writer.endElement("td");

            writer.startElement("td", null);
                
                writer.writeText("{% if (!o.files.error && !i && !o.options.autoUpload) { %}", null);
                    writer.startElement("button", null);
                    writer.writeAttribute("class", "start", null);
                    writer.writeText("Start", null);
                    writer.endElement("button");
                writer.writeText("{% } %}", null);
                
                writer.writeText("{% if (!i) { %}", null);
                    writer.startElement("button", null);
                    writer.writeAttribute("class", "cancel", null);
                    writer.writeText("Cancel", null);
                    writer.endElement("button");
                writer.writeText("{% } %}", null);
            writer.endElement("td");

        writer.endElement("tr");
    }
    
    public void encodeDefaultDownloadTemplate(FacesContext context, InputFiles component, String clientId) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("tr", null);
        writer.writeAttribute("class", "template-download fade", null);
            writer.startElement("td", null);
                writer.startElement("span", null);
                writer.writeAttribute("class", "preview", null);
                
                writer.writeText("{% if (file.thumbnailUrl) { %}", null);
                    writer.startElement("a", null);
                    writer.writeAttribute("href", "{%=file.url%}", null);
                    writer.writeAttribute("title", "{%=file.name%}", null);
                    writer.writeAttribute("download", "{%=file.name%}", null);
                    writer.writeAttribute("data-gallery", "data-gallery", null);
                        writer.startElement("img", null);
                        writer.writeAttribute("src", "{%=file.thumbnailUrl%}", null);
                        writer.endElement("img");
                    writer.endElement("a");
                writer.writeText("{% } %}", null);
                
                writer.endElement("span");
            writer.endElement("td");

            writer.startElement("td", null);
                writer.startElement("p", null);
                writer.writeAttribute("class", "name", null);
                
                    writer.writeText("{% if (file.thumbnailUrl) { %}", null);
                        writer.startElement("a", null);
                        writer.writeAttribute("href", "{%=file.url%}", null);
                        writer.writeAttribute("title", "{%=file.name%}", null);
                        writer.writeAttribute("download", "{%=file.name%}", null);
                        writer.writeAttribute("data-gallery", "data-gallery", null);
                        writer.writeText("{%=file.name%}", null);
                        writer.endElement("a");
                    writer.writeText("{% } else { %}", null);
                        writer.startElement("a", null);
                        writer.writeAttribute("href", "{%=file.url%}", null);
                        writer.writeAttribute("title", "{%=file.name%}", null);
                        writer.writeAttribute("download", "{%=file.name%}", null);
                        writer.writeText("{%=file.name%}", null);
                        writer.endElement("a");
                    writer.writeText("{% } %}", null);
                writer.endElement("p");
                
                writer.writeText("{% if (file.error) { %}", null);
                    writer.startElement("div", null);
                        writer.startElement("span", null);
                        writer.writeAttribute("class", "error", null);
                        writer.writeText("Error", null);
                        writer.endElement("span");
                        
                        writer.writeText("{%=file.error%}", null);
                    writer.endElement("div");
                writer.writeText("{% } %}", null);
            writer.endElement("td");

            writer.startElement("td", null);
                writer.startElement("p", null);
                writer.writeAttribute("class", "size", null);
                writer.writeText("{%=o.formatFileSize(file.size)%}", null);
                writer.endElement("p");
            writer.endElement("td");

            writer.startElement("td", null);

                writer.writeText("{% if (file.deleteWithCredentials) { %}", null);
                    writer.startElement("button", null);
                    writer.writeAttribute("class", "delete", null);
                    writer.writeAttribute("data-type", "{%=file.deleteType%}", null);
                    writer.writeAttribute("data-url", "{%=file.deleteUrl%}", null);
                    writer.writeAttribute("data-xhr-fields", "{'withCredentials':true}", null);
                    writer.writeText("Delete", null);
                    writer.endElement("button");
                writer.writeText("{% } else { %}", null);
                    writer.startElement("button", null);
                    writer.writeAttribute("class", "delete", null);
                    writer.writeAttribute("data-type", "{%=file.deleteType%}", null);
                    writer.writeAttribute("data-url", "{%=file.deleteUrl%}", null);
                    writer.writeText("Delete", null);
                    writer.endElement("button");
                writer.writeText("{% } %}", null);

                writer.startElement("input", null);
                writer.writeAttribute("type", "checkbox", null);
                writer.writeAttribute("name", "delete", null);
                writer.writeAttribute("value", "1", null);
                writer.writeAttribute("class", "toggle", null);
                writer.endElement("input");

           writer.endElement("td");

        writer.endElement("tr");
    }
    
    public void encodeScript(FacesContext context, InputFiles component, String clientId) throws IOException {
        StringBuilder scriptBuilder = new StringBuilder();
        scriptBuilder.append("BlazeFaces.cw('FileUpload',")
        .append("'").append(component.getClientId()).append("',")
        .append("{ id: '").append(component.getClientId()).append("',")
        .append("maxFileSize: ").append(component.getMaxFileSize()).append(",")
        .append("uploadTemplateId: '").append(clientId).append("_template_upload").append("',")
        .append("downloadTemplateId: '").append(clientId).append("_template_download").append("',")
        .append("dnd: false")
//      .append("acceptFileType: ").append(component.getAllowedExtensions())
        .append("});");
        
        RendererUtils.addBodyBottomScript(context, scriptBuilder.toString());
    }
}
