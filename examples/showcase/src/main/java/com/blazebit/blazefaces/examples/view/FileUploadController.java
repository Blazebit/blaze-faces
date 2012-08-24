package com.blazebit.blazefaces.examples.view;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import com.blazebit.blazefaces.event.FileUploadEvent;
import com.blazebit.blazefaces.model.UploadedFile;

@Named
@RequestScoped
public class FileUploadController {

	private UploadedFile file;

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void upload() {
		if (file != null) {
			FacesMessage msg = new FacesMessage("Succesful", file.getFileName()
					+ " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
}