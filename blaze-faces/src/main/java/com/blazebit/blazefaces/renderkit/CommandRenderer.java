/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.renderkit;

import java.util.Map;

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

    private static final String DEFAULT_TYPE = "submit";
    private static final String[] ALLOWED_TYPES = {
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

    protected String getType(FacesContext context, UIComponent component) {
        String inputType = (String) component.getAttributes().get("type");
        
        for(String type : ALLOWED_TYPES){
            if(type.equalsIgnoreCase(inputType))
                return type;
        }
        
        return DEFAULT_TYPE;
    }
}
