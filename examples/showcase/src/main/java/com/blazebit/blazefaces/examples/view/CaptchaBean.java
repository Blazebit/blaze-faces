package com.blazebit.blazefaces.examples.view;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

@Named
@RequestScoped
public class CaptchaBean {

	public void submit(ActionEvent event) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Correct", "Correct");

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
}
