/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.behavior.show;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.render.ClientBehaviorRenderer;

import com.blazebit.blazefaces.util.ComponentUtil;

/**
 *
 * @author Christian Beikov
 */
public class ShowBehaviorRenderer extends ClientBehaviorRenderer {

    @Override
    public String getScript(ClientBehaviorContext behaviorContext, ClientBehavior behavior) {
        ShowBehavior behav = (ShowBehavior) behavior;
        if (behav.isDisabled()) {
            return null;
        }

        FacesContext fc = behaviorContext.getFacesContext();
        UIComponent component = behaviorContext.getComponent();
        String source = behaviorContext.getSourceId();
        
        if (source == null) {
            source = component.getClientId(fc);
        }

        String forId = ComponentUtil.findComponentClientId(behav.getForId());
        StringBuilder req = new StringBuilder();
        req.append("$('");
        req.append(forId);
        req.append("').show();");

        return req.toString();
    }
}
