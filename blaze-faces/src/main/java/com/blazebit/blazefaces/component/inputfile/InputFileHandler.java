/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.inputfile;

import java.util.List;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.MetaRuleset;

import com.blazebit.blazefaces.event.FileUploadEvent;
import com.blazebit.blazefaces.facelets.MethodRule;

public class InputFileHandler extends ComponentHandler {

	public InputFileHandler(ComponentConfig config) {
		super(config);
	}
	
	@SuppressWarnings("rawtypes")
	protected MetaRuleset createMetaRuleset(Class type) { 
		MetaRuleset metaRuleset = super.createMetaRuleset(type); 
		Class<?>[] fileUploadEventClass = new Class[]{FileUploadEvent.class};
		
		metaRuleset.addRule(new MethodRule("fileUploadListener", List.class, fileUploadEventClass));
		
		return metaRuleset; 
	} 
}