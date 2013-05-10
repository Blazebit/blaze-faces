/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.behavior.hide;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.render.ClientBehaviorRenderer;

import com.blazebit.blazefaces.apt.JsfBehaviorRenderer;
import com.blazebit.blazefaces.util.ComponentUtils;

/**
 *
 * @author Christian Beikov
 */
//@JsfBehaviorRenderer
public class HideBehaviorRenderer extends ClientBehaviorRenderer {

    @Override
    public String getScript(ClientBehaviorContext behaviorContext, ClientBehavior behavior) {
        HideBehavior behav = (HideBehavior) behavior;
        if (behav.isDisabled()) {
            return null;
        }

        FacesContext fc = behaviorContext.getFacesContext();
        UIComponent component = behaviorContext.getComponent();
        String source = behaviorContext.getSourceId();
        
        if (source == null) {
            source = component.getClientId(fc);
        }

        String forId = ComponentUtils.findComponentClientId(behav.getForId());
        StringBuilder req = new StringBuilder();
        req.append("$('");
        req.append(forId);
        req.append("').hide();");

        return req.toString();
    }
}
