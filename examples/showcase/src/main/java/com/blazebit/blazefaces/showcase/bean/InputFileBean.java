/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.showcase.bean;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;
import javax.servlet.http.Part;

import com.blazebit.blazefaces.event.FileUploadEvent;
import com.blazebit.blazefaces.model.UploadedFile;

/**
 * 
 * @author Christian Beikov
 */
@Named
@ConversationScoped
public class InputFileBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Part file;
	private Part files[];

	public void preRender(ComponentSystemEvent event) {
		System.out.println("Render");
	}

	public void fileUploadListener(FileUploadEvent event) {
		System.out.println(event);
	}

	public String test() {
	    System.out.println(file);
	    return "";
	}

    
    public Part getFile() {
        return file;
    }

    
    public void setFile(Part file) {
        this.file = file;
    }

    public Part[] getFiles() {
		return files;
	}

	public void setFiles(Part[] files) {
		this.files = files;
	}

}
