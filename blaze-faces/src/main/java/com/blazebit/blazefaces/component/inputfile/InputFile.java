/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.inputfile;

import java.util.ArrayList;
import java.util.List;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;

import com.blazebit.blazefaces.event.FileUploadEvent;

@ResourceDependencies({
        
        @ResourceDependency(library="blazefaces", name="jquery/ui/jquery-ui.css"),
        @ResourceDependency(library="blazefaces", name="inputfile/inputfile.css"),
        @ResourceDependency(library="blazefaces", name="core/core.css"),
        @ResourceDependency(library="blazefaces", name="core/ui.css"),
        @ResourceDependency(library="blazefaces", name="panel/panel.css"),
        @ResourceDependency(library="blazefaces", name="commandbutton/commandButton.css"),
	@ResourceDependency(library="blazefaces", name="jquery/jquery.js"),
	@ResourceDependency(library="blazefaces", name="jquery/ui/jquery-ui.js"),
	@ResourceDependency(library="blazefaces", name="core/core.js"),
        @ResourceDependency(library="blazefaces", name="core/ajax.js"),
        
        @ResourceDependency(library="blazefaces", name="core/ui.js"),
        @ResourceDependency(library="blazefaces", name="commandbutton/commandButton.js"),
        @ResourceDependency(library="blazefaces", name="inputfile/FormReplacement.js"),
       
        @ResourceDependency(library="blazefaces", name="inputfile/Progressbar.js"),
        @ResourceDependency(library="blazefaces", name="inputfile/FileUploader.js")
        
})
public class InputFile extends UIInput implements ClientBehaviorHolder {


    public static final String COMPONENT_TYPE = "com.blazebit.blazefaces.component.InputFile";
    public static final String COMPONENT_FAMILY = "com.blazebit.blazefaces.component";
    private static final String DEFAULT_RENDERER = "com.blazebit.blazefaces.component.InputFileRenderer";
    private static final String OPTIMIZED_PACKAGE = "com.blazebit.blazefaces.component.";

    protected enum PropertyKeys {

            fileUploadListener
            ,container
            ,multiple
            ,cancelable
            ,showProgress
            ,autoUpload
            ,label
            ,allowTypes
            ,sizeLimit
            ,fileLimit
            ,showButtons
            ,style
            ,styleClass
            ,mode
            ,uploadLabel
            ,cancelLabel
            ,invalidSizeMessage
            ,invalidFileMessage
            ,fileLimitMessage
            ,dragAndDrop
            ,onstart
            ,oncomplete
            ,onprogress
            ,disabled;

            String toString;

            PropertyKeys(String toString) {
                    this.toString = toString;
            }

            PropertyKeys() {}

            @Override
            public String toString() {
                    return ((this.toString != null) ? this.toString : super.toString());
            }
    }

    public InputFile() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public javax.el.MethodExpression getFileUploadListener() {
        return (javax.el.MethodExpression) getStateHelper().eval(PropertyKeys.fileUploadListener, null);
    }

    public void setFileUploadListener(javax.el.MethodExpression _fileUploadListener) {
        getStateHelper().put(PropertyKeys.fileUploadListener, _fileUploadListener);
        handleAttribute("fileUploadListener", _fileUploadListener);
    }

    public java.lang.String getContainer() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.container, null);
    }

    public void setContainer(java.lang.String _container) {
        getStateHelper().put(PropertyKeys.container, _container);
        handleAttribute("container", _container);
    }

    public boolean isMultiple() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.multiple, false);
    }

    public void setMultiple(boolean _multiple) {
        getStateHelper().put(PropertyKeys.multiple, _multiple);
        handleAttribute("multiple", _multiple);
    }

    public boolean isCancelable() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.cancelable, true);
    }

    public void setCancelable(boolean _cancelable) {
        getStateHelper().put(PropertyKeys.cancelable, _cancelable);
        handleAttribute("cancelable", _cancelable);
    }

    public boolean isShowProgress() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.multiple, true);
    }

    public void setShowProgress(boolean _showProgress) {
        getStateHelper().put(PropertyKeys.showProgress, _showProgress);
        handleAttribute("showProgress", _showProgress);
    }

    public boolean isAutoUpload() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.autoUpload, false);
    }

    public void setAutoUpload(boolean _autoUpload) {
        getStateHelper().put(PropertyKeys.autoUpload, _autoUpload);
        handleAttribute("autoUpload", _autoUpload);
    }

    public java.lang.String getLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.label, "Choose");
    }

    public void setLabel(java.lang.String _label) {
        getStateHelper().put(PropertyKeys.label, _label);
        handleAttribute("label", _label);
    }

    public java.lang.String getAllowTypes() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.allowTypes, null);
    }

    public void setAllowTypes(java.lang.String _allowTypes) {
        getStateHelper().put(PropertyKeys.allowTypes, _allowTypes);
        handleAttribute("allowTypes", _allowTypes);
    }

    public int getSizeLimit() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.sizeLimit, java.lang.Integer.MAX_VALUE);
    }

    public void setSizeLimit(int _sizeLimit) {
        getStateHelper().put(PropertyKeys.sizeLimit, _sizeLimit);
        handleAttribute("sizeLimit", _sizeLimit);
    }

    public int getFileLimit() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.fileLimit, java.lang.Integer.MAX_VALUE);
    }

    public void setFileLimit(int _fileLimit) {
        getStateHelper().put(PropertyKeys.fileLimit, _fileLimit);
        handleAttribute("fileLimit", _fileLimit);
    }

    public boolean isShowButtons() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showButtons, false);
    }

    public void setShowButtons(boolean _showButtons) {
        getStateHelper().put(PropertyKeys.showButtons, _showButtons);
        handleAttribute("showButtons", _showButtons);
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
    }

    public java.lang.String getMode() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.mode, "advanced");
    }

    public void setMode(java.lang.String _mode) {
        getStateHelper().put(PropertyKeys.mode, _mode);
        handleAttribute("mode", _mode);
    }

    public java.lang.String getUploadLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.uploadLabel, "Upload");
    }

    public void setUploadLabel(java.lang.String _uploadLabel) {
        getStateHelper().put(PropertyKeys.uploadLabel, _uploadLabel);
        handleAttribute("uploadLabel", _uploadLabel);
    }

    public java.lang.String getCancelLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.cancelLabel, "Cancel");
    }

    public void setCancelLabel(java.lang.String _cancelLabel) {
        getStateHelper().put(PropertyKeys.cancelLabel, _cancelLabel);
        handleAttribute("cancelLabel", _cancelLabel);
    }

    public java.lang.String getInvalidSizeMessage() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.invalidSizeMessage, null);
    }

    public void setInvalidSizeMessage(java.lang.String _invalidSizeMessage) {
        getStateHelper().put(PropertyKeys.invalidSizeMessage, _invalidSizeMessage);
        handleAttribute("invalidSizeMessage", _invalidSizeMessage);
    }

    public java.lang.String getInvalidFileMessage() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.invalidFileMessage, null);
    }

    public void setInvalidFileMessage(java.lang.String _invalidFileMessage) {
        getStateHelper().put(PropertyKeys.invalidFileMessage, _invalidFileMessage);
        handleAttribute("invalidFileMessage", _invalidFileMessage);
    }

    public java.lang.String getFileLimitMessage() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.fileLimitMessage, null);
    }

    public void setFileLimitMessage(java.lang.String _fileLimitMessage) {
        getStateHelper().put(PropertyKeys.fileLimitMessage, _fileLimitMessage);
        handleAttribute("fileLimitMessage", _fileLimitMessage);
    }

    public boolean isDragAndDrop() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.dragAndDrop, true);
    }

    public void setDragAndDrop(boolean _dragAndDrop) {
        getStateHelper().put(PropertyKeys.dragAndDrop, _dragAndDrop);
        handleAttribute("dragAndDrop", _dragAndDrop);
    }

    public java.lang.String getOnstart() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onstart, null);
    }

    public void setOnstart(java.lang.String _onstart) {
        getStateHelper().put(PropertyKeys.onstart, _onstart);
        handleAttribute("onstart", _onstart);
    }

    public java.lang.String getOncomplete() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.oncomplete, null);
    }

    public void setOncomplete(java.lang.String _oncomplete) {
        getStateHelper().put(PropertyKeys.oncomplete, _oncomplete);
        handleAttribute("oncomplete", _oncomplete);
    }

    public java.lang.String getOnprogress() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onprogress, null);
    }

    public void setOnprogress(java.lang.String _onprogress) {
        getStateHelper().put(PropertyKeys.onprogress, _onprogress);
        handleAttribute("onprogress", _onprogress);
    }

    public boolean isDisabled() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.disabled, false);
    }

    public void setDisabled(boolean _disabled) {
        getStateHelper().put(PropertyKeys.disabled, _disabled);
        handleAttribute("disabled", _disabled);
    }

    @Override
    public void broadcast(javax.faces.event.FacesEvent event) throws javax.faces.event.AbortProcessingException {
        super.broadcast(event);

        FacesContext facesContext = FacesContext.getCurrentInstance();
        MethodExpression me = getFileUploadListener();

        if (me != null && event instanceof FileUploadEvent) {
            me.invoke(facesContext.getELContext(), new Object[]{event});
        }
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @SuppressWarnings("unchecked")
	public void handleAttribute(String name, Object value) {
        List<String> setAttributes = (List<String>) this.getAttributes().get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(OPTIMIZED_PACKAGE)) {
                setAttributes = new ArrayList<String>(6);
                this.getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
            }
        }
        if (setAttributes != null) {
            if (value == null) {
                ValueExpression ve = getValueExpression(name);
                if (ve == null) {
                    setAttributes.remove(name);
                } else if (!setAttributes.contains(name)) {
                    setAttributes.add(name);
                }
            }
        }
    }
}
