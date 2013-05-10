/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.behavior.invoke;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.render.ClientBehaviorRenderer;

import com.blazebit.blazefaces.apt.JsfBehaviorRenderer;

/**
 *
 * @author Christian Beikov
 */
//@JsfBehaviorRenderer
public class InvokeBehaviorRenderer extends ClientBehaviorRenderer {

    @Override
    public String getScript(ClientBehaviorContext behaviorContext, ClientBehavior behavior) {
        InvokeBehavior behav = (InvokeBehavior) behavior;
        if (behav.isDisabled()) {
            return null;
        }

        FacesContext fc = behaviorContext.getFacesContext();
        UIComponent component = behaviorContext.getComponent();
        String source = behaviorContext.getSourceId();
        if (source == null) {
            source = component.getClientId(fc);
        }

        StringBuilder req = new StringBuilder();
        req.append("(function(source){");
        req.append(behav.getCode());
        req.append(";})(");
        req.append("'").append(source).append("'");
        req.append(");");

        return req.toString();
    }
}
