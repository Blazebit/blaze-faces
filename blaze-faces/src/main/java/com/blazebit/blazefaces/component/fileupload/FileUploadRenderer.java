/*
 * Copyright 2011-2012 Blazebit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blazebit.blazefaces.component.fileupload;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.servlet.ServletRequestWrapper;

import org.apache.commons.fileupload.FileItem;
import com.blazebit.blazefaces.event.FileUploadEvent;
import com.blazebit.blazefaces.model.DefaultUploadedFile;
import com.blazebit.blazefaces.renderkit.CoreRenderer;
import com.blazebit.blazefaces.util.ComponentUtils;
import com.blazebit.blazefaces.util.HTML;
import com.blazebit.blazefaces.webapp.MultipartRequest;

public class FileUploadRenderer extends CoreRenderer {

    @Override
	public void decode(FacesContext context, UIComponent component) {
		FileUpload fileUpload = (FileUpload) component;
        String clientId = fileUpload.getClientId(context);
		MultipartRequest multipartRequest = getMultiPartRequestInChain(context);
		
		if(multipartRequest != null) {
			FileItem file = multipartRequest.getFileItem(clientId);

            if(fileUpload.getMode().equals("simple")) {
                decodeSimple(context, fileUpload, file);
            }
            else {
                decodeAdvanced(context, fileUpload, file);
            }
		}
    }
	
	public void decodeSimple(FacesContext context, FileUpload fileUpload, FileItem file) {
		if(file.getName().equals(""))
            fileUpload.setSubmittedValue("");
        else
            fileUpload.setSubmittedValue(new DefaultUploadedFile(file));
	}
    
    public void decodeAdvanced(FacesContext context, FileUpload fileUpload, FileItem file) {
		if(file != null) {
            fileUpload.queueEvent(new FileUploadEvent(fileUpload, new DefaultUploadedFile(file)));
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
		FileUpload fileUpload = (FileUpload) component;
		
		encodeMarkup(context, fileUpload);
		encodeScript(context, fileUpload);
	}

	protected void encodeScript(FacesContext context, FileUpload fileUpload) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = fileUpload.getClientId(context);
        String mode = fileUpload.getMode();
				
        startScript(writer, clientId);

        writer.write("$(function(){");
        
        writer.write("BlazeFaces.cw('FileUpload','" + fileUpload.resolveWidgetVar() + "',{");
        writer.write("id:'" + clientId + "'");
        writer.write(",mode:'" + mode + "'");
        
        if(!mode.equals("simple")) {
            String update = fileUpload.getUpdate();
            String process = fileUpload.getProcess();
            
            writer.write(",autoUpload:" + fileUpload.isAuto());
            writer.write(",dnd:" + fileUpload.isDragDropSupport());
            
            if(update != null) writer.write(",update:'" + ComponentUtils.findClientIds(context, fileUpload, update) + "'");
            if(process != null) writer.write(",process:'" + ComponentUtils.findClientIds(context, fileUpload, process) + "'");
            
            if(fileUpload.getOnstart() != null) writer.write(",onstart:function() {" + fileUpload.getOnstart() + ";}");
            if(fileUpload.getOncomplete() != null) writer.write(",oncomplete:function() {" + fileUpload.getOncomplete() + ";}");
            
            //restrictions
            if(fileUpload.getAllowTypes() != null) writer.write(",acceptFileTypes:" + fileUpload.getAllowTypes());
            if(fileUpload.getSizeLimit() != Integer.MAX_VALUE) writer.write(",maxFileSize:" + fileUpload.getSizeLimit());
            
            //restriction messages
            if(fileUpload.getInvalidFileMessage() != null) writer.write(",invalidFileMessage:'" + fileUpload.getInvalidFileMessage() + "'");
            if(fileUpload.getInvalidSizeMessage() != null) writer.write(",invalidSizeMessage:'" + fileUpload.getInvalidSizeMessage() + "'");
        }

		writer.write("},'fileupload');});");
		
		endScript(writer);
	}

	protected void encodeMarkup(FacesContext context, FileUpload fileUpload) throws IOException {
		if(fileUpload.getMode().equals("simple"))
            encodeSimpleMarkup(context, fileUpload);
        else
            encodeAdvancedMarkup(context, fileUpload);
	}

    protected void encodeAdvancedMarkup(FacesContext context, FileUpload fileUpload) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
		String clientId = fileUpload.getClientId(context);
        String styleClass = fileUpload.getStyleClass();
        styleClass = styleClass == null ? FileUpload.CONTAINER_CLASS : FileUpload.CONTAINER_CLASS + " " + styleClass;

		writer.startElement("div", fileUpload);
		writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "id");
        if(fileUpload.getStyle() != null) 
            writer.writeAttribute("style", fileUpload.getStyle(), "style");
        
        //buttonbar
        writer.startElement("div", fileUpload);
        writer.writeAttribute("class", FileUpload.BUTTON_BAR_CLASS, "styleClass");

        //choose button
        encodeChooseButton(context, fileUpload);
        
        if(fileUpload.isShowButtons() && !fileUpload.isAuto()) {
            encodeButton(context, fileUpload.getUploadLabel(), FileUpload.UPLOAD_BUTTON_CLASS, "ui-icon-arrowreturnthick-1-n");
            encodeButton(context, fileUpload.getCancelLabel(), FileUpload.CANCEL_BUTTON_CLASS, "ui-icon-cancel");
        }
        
        writer.endElement("div");
        
        //content
        writer.startElement("div", null);
        writer.writeAttribute("class", FileUpload.CONTENT_CLASS, null);
        
        writer.startElement("table", null);
        writer.writeAttribute("class", FileUpload.FILES_CLASS, null);
        writer.endElement("table");
        
        writer.endElement("div");

		writer.endElement("div");
    }

    protected void encodeSimpleMarkup(FacesContext context, FileUpload fileUpload) throws IOException {
        encodeInputField(context, fileUpload, fileUpload.getClientId(context));
    }
    
    protected void encodeChooseButton(FacesContext context, FileUpload fileUpload) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = fileUpload.getClientId(context);
        
        writer.startElement("label", null);
        writer.writeAttribute("class", HTML.BUTTON_TEXT_ICON_LEFT_BUTTON_CLASS + " " + FileUpload.CHOOSE_BUTTON_CLASS, null);
        
        //button icon
        writer.startElement("span", null);
        writer.writeAttribute("class", HTML.BUTTON_LEFT_ICON_CLASS + " ui-icon-plusthick", null);
        writer.endElement("span");
        
        //text
        writer.startElement("span", null);
        writer.writeAttribute("class", HTML.BUTTON_TEXT_CLASS, null);
        writer.writeText(fileUpload.getLabel(), "value");
        writer.endElement("span");

        encodeInputField(context, fileUpload, clientId + "_input");
        
		writer.endElement("label");
    }

    protected void encodeInputField(FacesContext context, FileUpload fileUpload, String clientId) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        writer.startElement("input", null);
		writer.writeAttribute("type", "file", null);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("name", clientId, null);
        
        if(fileUpload.isMultiple()) writer.writeAttribute("multiple", "multiple", null);
        if(fileUpload.getStyle() != null) writer.writeAttribute("style", fileUpload.getStyle(), "style");
        if(fileUpload.getStyleClass() != null) writer.writeAttribute("class", fileUpload.getStyleClass(), "styleClass");
        if(fileUpload.isDisabled()) writer.writeAttribute("disabled", "disabled", "disabled");
        
		writer.endElement("input");
    }
    
    protected void encodeButton(FacesContext context, String label, String styleClass, String icon) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        writer.startElement("button", null);
		writer.writeAttribute("type", "button", null);
        writer.writeAttribute("class", HTML.BUTTON_TEXT_ICON_LEFT_BUTTON_CLASS + " " + styleClass, null);
        
        //button icon
        String iconClass = HTML.BUTTON_LEFT_ICON_CLASS ;
        writer.startElement("span", null);
        writer.writeAttribute("class", iconClass + " " + icon, null);
        writer.endElement("span");
        
        //text
        writer.startElement("span", null);
        writer.writeAttribute("class", HTML.BUTTON_TEXT_CLASS, null);
        writer.writeText(label, "value");
        writer.endElement("span");

		writer.endElement("button");
    }

    /**
     * Return null if no file is submitted in simple mode
     * 
     * @param context
     * @param component
     * @param submittedValue
     * @return
     * @throws ConverterException 
     */
    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        FileUpload fileUpload = (FileUpload) component;
        
        if(fileUpload.getMode().equals("simple") && submittedValue != null && submittedValue.equals("")) {
            return null;
        }
        else {
            return submittedValue;
        }
    } 
    
    
    // Old implementation
    // TODO: merge code
//    public static final String INPUT_FILE_CLASS = "file-input hidden";
//    
//    @Override
//    public void decode(FacesContext ctx, UIComponent component) {
//        InputFile fileUpload = (InputFile) component;
//        
//        if(ComponentUtils.componentIsDisabledOrReadonly(component))
//            return;
//        
//        String clientId = fileUpload.getClientId(ctx);
//        MultipartRequest multipartRequest = getMultiPartRequestInChain(ctx);
//        
//        if (multipartRequest != null) {
//            List<FileItem> files = multipartRequest.getFileItems(clientId);
//            
//            if (files != null && !files.isEmpty()) {
//                List<UploadedFile> uploadedFiles = new ArrayList<UploadedFile>();
//                
//                for (FileItem file : files) {
//                    uploadedFiles.add(new DefaultUploadedFile(file));
//                }
//                
//                if (fileUpload.isMultiple()) {
//                    if ("simple".equals(fileUpload.getMode())) {
//                        Class<?> fieldType = fileUpload.getValueExpression("value").getType(ctx.getELContext());
//                        fileUpload.setSubmittedValue(fieldType.isArray() ? uploadedFiles.toArray() : uploadedFiles);
//                    } else {
//                        fileUpload.queueEvent(new FileUploadEvent(fileUpload, uploadedFiles));
//                    }
//                } else {
//                    if ("simple".equals(fileUpload.getMode())) {
//                        fileUpload.setSubmittedValue(uploadedFiles.get(0));
//                    } else {
//                        fileUpload.queueEvent(new FileUploadEvent(fileUpload, uploadedFiles.get(0)));
//                    }
//                }
//            }
//        }
//    }
//
//    private MultipartRequest getMultiPartRequestInChain(FacesContext ctx) {
//        Object request = ctx.getExternalContext().getRequest();
//        
//        while (request instanceof ServletRequestWrapper) {
//            if (request instanceof MultipartRequest) {
//                return (MultipartRequest) request;
//            } else {
//                request = ((ServletRequestWrapper) request).getRequest();
//            }
//        }
//        
//        return null;
//    }
//    
//    @Override
//    public void encodeEnd(FacesContext ctx, UIComponent component) throws IOException {
//        InputFile fileUpload = (InputFile) component;
//        
//        if("simple".equals(fileUpload.getMode()))
//            encodeSimple(ctx, fileUpload);
//        else
//            encodeAdvanced(ctx, fileUpload);
//        
//        encodeScript(ctx, fileUpload);
//    }
//    
//    protected void encodeScript(FacesContext context, InputFile fileUpload) throws IOException {
//        ResponseWriter writer = context.getResponseWriter();
//        String clientId = fileUpload.getClientId(context);
//        String containerId = clientId + "_container";
//        
//        if(fileUpload.getContainer() != null){
//            containerId = ComponentUtils.findComponentClientId(fileUpload.getContainer());
//        }
//        
//        writer.startElement("script", fileUpload);
//        writer.writeAttribute("type", "text/javascript", null);
//        
//        writer.write("jQuery(document).ready(function() {");
//        writer.write("new BlazeJS.UI.FormReplacement( { button : '.blaze-ui-bttn' } );");
//        
//        writer.write("new BlazeJS.Modules.FileUploader( {");
//        
//        writer.write("id : '" + clientId + "'");
//        writer.write(",container : " + containerId);
//        writer.write(",autoUpload : " + fileUpload.isAutoUpload());
//        writer.write(",dragAndDrop : " + fileUpload.isDragAndDrop());
//        writer.write(",supportsDragAndDrop : " + FeatureDetectionUtils.isSupported("draganddrop"));
//        writer.write(",mode : '" + fileUpload.getMode() + "'");
//        writer.write(",cancelLabel : '" + fileUpload.getCancelLabel() + "'");
//        
//        writer.write(",onstart : '" + fileUpload.getOnstart() + "'");
//        writer.write(",oncomplete : '" + fileUpload.getOncomplete() + "'");
//        writer.write(",onprogress : '" + fileUpload.getOnprogress() + "'");
//        
//        if (fileUpload.getAllowTypes() != null) {
//            writer.write(",allowTypes : '" + fileUpload.getAllowTypes() + "'");
//        }
//        if (fileUpload.getSizeLimit() != java.lang.Integer.MAX_VALUE) {
//            writer.write(",sizeLimit : '" + fileUpload.getSizeLimit() + "'");
//        }
//        if (fileUpload.getFileLimit() != java.lang.Integer.MAX_VALUE) {
//            writer.write(",fileLimit : '" + fileUpload.getFileLimit() + "'");
//        }
//        
//        if (fileUpload.getInvalidFileMessage() != null) {
//            writer.write(",invalidFileMessage : '" + fileUpload.getInvalidFileMessage() + "'");
//        }
//        if (fileUpload.getInvalidSizeMessage() != null) {
//            writer.write(",invalidSizeMessage : '" + fileUpload.getInvalidSizeMessage() + "'");
//        }
//        if (fileUpload.getFileLimitMessage() != null) {
//            writer.write(",fileLimitMessage : '" + fileUpload.getFileLimitMessage() + "'");
//        }
//        
//        writer.write(",cancelable : '" + fileUpload.isCancelable() + "'");
//        writer.write(",showProgress : '" + fileUpload.isShowProgress() + "'");
//        writer.write(",multiple : " + fileUpload.isMultiple() + "");
//        writer.write(",supportsMultiple : " + FeatureDetectionUtils.isSupported("input.multiple") + "");
//        
//        writer.write(",overviewTemplate : '" + encodeOverviewTemplate(context, fileUpload) + "'");
//        writer.write(",itemTemplate : '" + encodeItemTemplate(context, fileUpload) + "'");
//
//        // @TODO: Maybe add portlet support
//        writer.write(",cookie : '" + ((ServletContext) context.getExternalContext().getContext()).getSessionCookieConfig().getName());
//        writer.write("=" + ((HttpSession) (context.getExternalContext().getSession(true))).getId() + "'");
//        writer.write("});");
//        
//        writer.write("});");
//        
//        writer.endElement("script");
//    }
//    
//    protected String encodeOverviewTemplate(FacesContext ctx, InputFile fileUpload) throws IOException {
//        if (fileUpload.getFacet("overview") != null) {
//            return RendererUtils.encodeComponent(ctx, fileUpload.getFacet("overview"));
//        } else {
//            return RendererUtils.encodeToString(ctx, new BlazeEncoder() {
//                
//                public void encode(FacesContext ctx, Map<String, Object> attributes) throws IOException {
//                    ResponseWriter writer = ctx.getResponseWriter();
//                    writer.startElement("a", null);
//                    writer.writeAttribute("href", "#", null);
//                    writer.writeAttribute("class", "toggleFileTable icon-arrow-up-small tooltip", null);
//                    writer.writeAttribute("title", "Dateien Tabelle schließen", null);
//                    writer.endElement("a");
//                    
//                    writer.write("{progress}");
//                    
//                    writer.startElement("div", null);
//                    writer.writeAttribute("class", "meta", null);
//                    
//                    writer.startElement("span", null);
//                    writer.writeAttribute("class", "p", null);
//                    writer.write("{percent}%");
//                    writer.endElement("span");
//                    
//                    writer.startElement("span", null);
//                    writer.writeAttribute("class", "upgeloaded", null);
//                    writer.write(" von {filesize} MB");
//                    writer.endElement("span");
//                    
//                    writer.startElement("span", null);
//                    writer.writeAttribute("class", "speed", null);
//                    writer.write("{speed}");
//                    writer.endElement("span");
//                    
//                    writer.startElement("span", null);
//                    writer.writeAttribute("class", "time-remaining", null);
//                    writer.write("{timeremain}");
//                    writer.endElement("span");
//                    
//                    writer.endElement("div");
//                    
//                    writer.startElement("div", null);
//                    writer.writeAttribute("class", "amountFiles tooltip", null);
//                    
//                    writer.startElement("span", null);
//                    writer.write("{filecount}");
//                    writer.endElement("span");
//                    
//                    writer.endElement("div");
//                }
//            });
//        }
//    }
//    
//    protected String encodeItemTemplate(FacesContext ctx, InputFile fileUpload) throws IOException {
//        if (fileUpload.getFacet("item") != null) {
//            return RendererUtils.encodeComponent(ctx, fileUpload.getFacet("item"));
//        } else {
//            return RendererUtils.encodeToString(ctx, new BlazeEncoder() {
//                
//                public void encode(FacesContext ctx, Map<String, Object> attributes) throws IOException {
//                    ResponseWriter writer = ctx.getResponseWriter();
//                    writer.write("{cancel}");
//                    writer.write("{progress}");
//                    
//                    writer.startElement("div", null);
//                    writer.writeAttribute("class", "meta", null);
//                    
//                    writer.startElement("span", null);
//                    writer.writeAttribute("class", "p", null);
//                    writer.write("{percent}%");
//                    writer.endElement("span");
//                    
//                    writer.startElement("span", null);
//                    writer.writeAttribute("class", "upgeloaded", null);
//                    writer.write(" von {filesize} MB");
//                    writer.endElement("span");
//                    
//                    writer.startElement("span", null);
//                    writer.writeAttribute("class", "speed", null);
//                    writer.write("{speed}");
//                    writer.endElement("span");
//                    
//                    writer.endElement("div");
//                    
//                    writer.startElement("div", null);
//                    writer.writeAttribute("class", "file-name", null);
//                    
//                    writer.startElement("span", null);
//                    writer.write("{filename}");
//                    writer.endElement("span");
//                    
//                    writer.endElement("div");
//                }
//            });
//        }
//    }
//    
//    protected void encodeAdvanced(FacesContext ctx, InputFile fileUpload) throws IOException {
//        ResponseWriter response = ctx.getResponseWriter();
//        String clientId = fileUpload.getClientId(ctx);
//        response.startElement("div", fileUpload);
//        response.writeAttribute("id", clientId, "id");
//        response.writeAttribute("class", "blaze-ui-form blaze-ui-bttn", null);
//        
//        response.startElement("div", null);
//        response.writeAttribute("class", "blaze-ui-bg lft", null);
//        response.endElement("div");
//        
//        response.startElement("div", null);
//        response.writeAttribute("class", "blaze-ui-bg mdl", null);
//        
//        response.startElement("span", null);
//        response.writeAttribute("class", "icon-plus-rounded-inset icon", null);
//        response.endElement("span");
//        
//        response.startElement("a", null);
//        response.writeAttribute("href", "#", null);
//        response.write("Hinzufügen...");
//        response.endElement("a");
//        
//        response.endElement("div");
//        
//        response.startElement("div", null);
//        response.writeAttribute("class", "blaze-ui-bg rgt", null);
//        response.endElement("div");
//        
//        encodeInputField(ctx, fileUpload, clientId + "_input");
//        
//        response.endElement("div");
//        
//        if(fileUpload.getContainer() == null)
//            encodeContainer(ctx, fileUpload, clientId + "_container");
//    }
//    
//    protected void encodeSimple(FacesContext context, InputFile fileUpload) throws IOException {
//        encodeInputField(context, fileUpload, fileUpload.getClientId(context) + "_input");
//        encodeBehaviors(context, (ClientBehaviorHolder) fileUpload);
//    }
//    
//    protected void encodeContainer(FacesContext ctx, InputFile fileUpload, String clientId) throws IOException {
//        BlazeEncoder encoder = new PanelEncoder();
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("id", clientId);
//        map.put("title", "Medienupload");
//        map.put("chain", new BlazeEncoder() {
//
//            public void encode(FacesContext ctx, Map<String, Object> attributes) throws IOException {
//                ResponseWriter response = ctx.getResponseWriter();
//                response.startElement("div", null);
//                response.writeAttribute("class", "form-content", null);
//
//                    response.startElement("div", null);
//                    response.writeAttribute("class", "tab-wrapper", null);
//                        response.startElement("div", null);
//                        response.writeAttribute("class", "empty-placeholder", null);
//                            response.startElement("p", null);
//                            response.write("Es wurden noch keine Medien ausgewählt!");
//                            response.endElement("p");
//                        response.endElement("div");
//                    response.endElement("div");
//
//                    response.startElement("div", null);
//                    response.writeAttribute("class", "controls", null);
//                        response.startElement("div", null);
//                        response.writeAttribute("class", "lft", null);
//                        response.endElement("div");
//
//                        response.startElement("div", null);
//                        response.writeAttribute("class", "mdl", null);
//                            response.startElement("div", null);
//                            response.writeAttribute("class", "blaze-ui-form blaze-ui-bttn upload-files", null);
//                                response.startElement("div", null);
//                                response.writeAttribute("class", "blaze-ui-bg lft", null);
//                                response.endElement("div");
//
//                                response.startElement("div", null);
//                                response.writeAttribute("class", "blaze-ui-bg mdl", null);
//                                    response.startElement("input", null);
//                                    response.writeAttribute("type", "submit", null);
//                                    response.writeAttribute("value", "Dateien Hochladen", null);
//                                    response.writeAttribute("disabled", "disabled", null);
//                                    response.endElement("input");
//                                response.endElement("div");
//
//                                response.startElement("div", null);
//                                response.writeAttribute("class", "blaze-ui-bg rgt", null);
//                                response.endElement("div");
//                            response.endElement("div");
//                        response.endElement("div");
//
//                        response.startElement("div", null);
//                        response.writeAttribute("class", "rgt", null);
//                        response.endElement("div");
//                    response.endElement("div");
//
//                response.endElement("div");
//            }
//        });
//        
//        encoder.encode(ctx, map);
//    }
//    
//    protected void encodeInputField(FacesContext context, InputFile fileUpload, String clientId) throws IOException {
//        ResponseWriter writer = context.getResponseWriter();
//        String styleClass = fileUpload.getStyleClass();
//        styleClass = styleClass == null ? INPUT_FILE_CLASS : INPUT_FILE_CLASS + " " + styleClass;
//        
//        writer.startElement("input", null);
//        writer.writeAttribute("type", "file", null);
//        writer.writeAttribute("id", clientId, null);
//        writer.writeAttribute("name", clientId, null);
//        
//        if (fileUpload.isMultiple() && FeatureDetectionUtils.isSupported("input.multiple")) {
//            writer.writeAttribute("multiple", "multiple", null);
//        }
//        if (fileUpload.getStyleClass() != null) {
//            writer.writeAttribute("class", styleClass, "styleClass");
//        }
//        if (fileUpload.isDisabled()) {
//            writer.writeAttribute("disabled", "disabled", "disabled");
//        }
//        renderPassThruAttributes(context, fileUpload, HTML5.COMMON_ATTRIBUTES);
//        
//        writer.endElement("input");
//    }
}