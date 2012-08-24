/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.examples.view;

import java.io.Serializable;

import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;

import com.blazebit.blazefaces.event.FileUploadEvent;
import com.blazebit.blazefaces.model.UploadedFile;

/**
 * 
 * @author Christian Beikov
 */
@Named
@ViewAccessScoped
public class InputFileBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private UploadedFile files[];

	public void preRender(ComponentSystemEvent event) {
		System.out.println("Render");
	}

	public void fileUploadListener(FileUploadEvent event) {
		System.out.println(event);
	}

	public UploadedFile[] getFiles() {
		return files;
	}

	public void setFiles(UploadedFile[] files) {
		this.files = files;
	}

}
