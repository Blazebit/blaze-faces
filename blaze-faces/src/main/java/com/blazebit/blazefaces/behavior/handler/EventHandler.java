/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.behavior.handler;

import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;

public class EventHandler extends UIComponentBase implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "com.blazebit.blazefaces.behavior.handler.EventHandler";
    public static final String COMPONENT_FAMILY = "com.blazebit.blazefaces.behavior";
    private static final String DEFAULT_RENDERER = "com.blazebit.blazefaces.behavior.handler.EventHandlerRenderer";

    public EventHandler() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public void addClientBehavior(String eventName, ClientBehavior behavior) {
        getChildren().add(new DefinitionOrderComponent(behavior));
    }

    @Override
    public String getDefaultEventName() {
        return "action";
    }
    
    public static class DefinitionOrderComponent extends UIComponentBase{

        public static final String COMPONENT_FAMILY = "com.blazebit.blazefaces.behavior";
        private ClientBehavior behavior; 
        
        public DefinitionOrderComponent(ClientBehavior behavior){
            this.behavior = behavior;
        }
        
        @Override
        public String getFamily() {
            return COMPONENT_FAMILY;
        }

        public ClientBehavior getBehavior() {
            return behavior;
        }
        
    }

}
