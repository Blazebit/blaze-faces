/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

import com.blazebit.blazefaces.model.UploadedFile;
import java.util.Arrays;
import java.util.List;

public class FileUploadEvent extends FacesEvent {

    private List<UploadedFile> files;

    public FileUploadEvent(UIComponent component, UploadedFile file) {
        this(component, Arrays.asList(file));
    }

    public FileUploadEvent(UIComponent component, List<UploadedFile> files) {
        super(component);
        this.files = files;
    }

    @Override
    public boolean isAppropriateListener(FacesListener listener) {
        return false;
    }

    @Override
    public void processListener(FacesListener listener) {
        throw new UnsupportedOperationException();
    }

    public List<UploadedFile> getFiles() {
        return files;
    }
}
