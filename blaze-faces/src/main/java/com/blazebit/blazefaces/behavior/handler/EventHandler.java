/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.behavior.handler;

import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;

import com.blazebit.blazefaces.apt.JsfComponent;

//@JsfComponent(
//		attributes = {
//			<attribute>
//	            <description><![CDATA[The name of the event when the action is triggered.]]>
//	            </description>
//	            <name>event</name>
//	            <required>false</required>
//	            <type>java.lang.String</type>
//	        </attribute>
//	        <attribute>
//	            <description><![CDATA[The type of the event handler. Valid values are parallel and sequential(default).]]>
//	            </description>
//	            <name>type</name>
//	            <required>false</required>
//	            <type>java.lang.String</type>
//	        </attribute>
//	        <attribute>
//	            <description><![CDATA[When set to true, the handler action will be called asynchronously.]]>
//	            </description>
//	            <name>async</name>
//	            <required>false</required>
//	            <type>java.lang.Boolean</type>
//	        </attribute>
//	        <attribute>
//	            <description><![CDATA[]]>
//	            </description>
//	            <name>disabled</name>
//	            <required>false</required>
//	            <type>java.lang.Boolean</type>
//	        </attribute>
//		}
//)
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
