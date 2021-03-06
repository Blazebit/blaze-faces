/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.commons.fileupload.FileItem;

/**
 * 
 * UploadedFile implementation based on Commons FileUpload FileItem
 */
public class DefaultUploadedFile implements UploadedFile, Serializable {

	private static final long serialVersionUID = 1L;
	
	private FileItem fileItem;

    public DefaultUploadedFile() {
    }

    public DefaultUploadedFile(FileItem fileItem) {
        this.fileItem = fileItem;
    }

    public String getFileName() {
        return fileItem.getName();
    }

    public InputStream getInputStream() throws IOException {
        return fileItem.getInputStream();
    }

    public long getSize() {
        return fileItem.getSize();
    }

    public byte[] getContents() {
        return fileItem.get();
    }

    public String getContentType() {
        return fileItem.getContentType();
    }
}
