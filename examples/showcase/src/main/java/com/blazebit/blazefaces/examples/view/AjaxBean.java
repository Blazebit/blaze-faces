/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.examples.view;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Named;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;

/**
 * 
 * @author Christian Beikov
 */
@Named
@ViewAccessScoped
public class AjaxBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public String getTime() {
		return new Date().toString();
	}
}
