/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.examples.view;

import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

/**
 * 
 * @author Christian Beikov
 */
@Named
@ConversationScoped
public class AjaxBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public String getTime() {
		return new Date().toString();
	}
}
