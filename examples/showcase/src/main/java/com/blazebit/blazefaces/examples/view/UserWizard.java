package com.blazebit.blazefaces.examples.view;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;

import com.blazebit.blazefaces.event.FlowEvent;
import com.blazebit.blazefaces.examples.domain.User;

@Named
@ViewAccessScoped
public class UserWizard implements Serializable {

	private static final long serialVersionUID = 1L;

	private User user = new User();

	private boolean skip;

	private static Logger logger = Logger.getLogger(UserWizard.class.getName());

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void save(ActionEvent actionEvent) {
		// Persist user

		FacesMessage msg = new FacesMessage("Successful", "Welcome :"
				+ user.getFirstname());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public String onFlowProcess(FlowEvent event) {
		logger.info("Current wizard step:" + event.getOldStep());
		logger.info("Next step:" + event.getNewStep());

		if (skip) {
			skip = false; // reset in case user goes back
			return "confirm";
		} else {
			return event.getNewStep();
		}
	}
}