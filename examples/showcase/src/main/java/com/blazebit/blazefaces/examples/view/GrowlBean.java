package com.blazebit.blazefaces.examples.view;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import com.blazebit.blazefaces.push.PushContext;
import com.blazebit.blazefaces.push.PushContextFactory;

@Named
@RequestScoped
public class GrowlBean {

	private String text;

	private String summary;

	private String detail;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void save(ActionEvent actionEvent) {
		FacesContext context = FacesContext.getCurrentInstance();

		context.addMessage(null,
				new FacesMessage("Successful", "Hello " + text));
		context.addMessage(null, new FacesMessage("Second Message",
				"Additional Info Here..."));
	}

	public void send() {
		PushContext pushContext = PushContextFactory.getDefault()
				.getPushContext();

		pushContext.push("/notifications", new FacesMessage(summary, detail));
	}
}
