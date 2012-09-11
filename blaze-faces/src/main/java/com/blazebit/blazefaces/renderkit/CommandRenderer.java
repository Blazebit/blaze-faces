/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.renderkit;

import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.blazebit.blazefaces.util.ComponentUtils;
import com.blazebit.blazefaces.util.RendererUtils;

/**
 *
 * @author Christian Beikov
 */
public class CommandRenderer extends OutputRenderer {

    protected static final String DEFAULT_TYPE = "submit";
    protected static final String[] ALLOWED_TYPES = {
        "submit",
        "reset"
    };
    
    public CommandRenderer() {
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        if (ComponentUtils.componentIsDisabledOrReadonly(component)) {
            return;
        }
        
        decodeBehaviors(context, component);
        String clientId = component.getClientId(context);
        
        if (wasClicked(context, component, clientId) && !isReset(component)) {
            component.queueEvent(new ActionEvent(component));
        }
    }

    protected static boolean isReset(UIComponent component) {
        return "reset".equals(component.getAttributes().get("type"));
    }

    protected static boolean wasClicked(FacesContext context, UIComponent component, String clientId) {
        if (clientId == null) {
            clientId = component.getClientId(context);
        }
        
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.get(clientId) == null) {
            // check if behavior was invoked
            if (RendererUtils.isPartialOrBehaviorAction(context, clientId)) {
                return true;
            }
            return false;
        }
        return true;
    }
    
    protected String[] getAllowedTypes(){
    	return ALLOWED_TYPES;
    }

    protected String getType(FacesContext context, UIComponent component) {
        return getType(context, (String) component.getAttributes().get("type"));
    }

    protected String getType(FacesContext context, String inputType) {        
        if(inputType != null && !inputType.isEmpty()){
	        for(String type : getAllowedTypes()){
	            if(type.equalsIgnoreCase(inputType))
	                return type;
	        }
	        
	        throw new FacesException("Invalid command type '" + inputType + "' given.");
        }
        
        return DEFAULT_TYPE;
    }
}
