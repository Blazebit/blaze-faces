/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.behavior.ajax;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.event.AjaxBehaviorListener;

import com.blazebit.blazefaces.apt.JsfAttribute;
import com.blazebit.blazefaces.apt.JsfBehavior;
import com.blazebit.blazefaces.apt.JsfDescription;
import com.blazebit.blazefaces.behavior.BaseBehavior;

/**
 *
 * @author Christian Beikov
 */
@ResourceDependencies({
	@ResourceDependency(library="blazefaces", name="jquery/jquery.js"),
	@ResourceDependency(library="blazefaces", name="blazefaces.js")
})
@JsfBehavior(
        tag = "ajax",
        handler = AjaxBehaviorHandler.class,
        renderer = AjaxBehaviorRenderer.class,
        hints = ClientBehaviorHint.SUBMITTING,
        attributes = {
            @JsfAttribute(name = "listener", type = MethodExpression.class, description = @JsfDescription(description = "Method to process in partial request.")),
            @JsfAttribute(name = "async", type = boolean.class, defaultValue="false", description = @JsfDescription(description = "When set to true, ajax requests are not queued.")),
            @JsfAttribute(name = "process", type = String.class, defaultValue="@this", description = @JsfDescription(description = "Component(s) to process in partial request. Defaults to @this.")),
            @JsfAttribute(name = "update", type = String.class, description = @JsfDescription(description = "Component(s) to update with ajax.")),
            @JsfAttribute(name = "onstart", type = String.class, description = @JsfDescription(description = "Client side callback execute before ajax request is begins.")),
            @JsfAttribute(name = "oncomplete", type = String.class, description = @JsfDescription(description = "Client side callback execute when ajax request is completed and dom is updated.")),
            @JsfAttribute(name = "onerror", type = String.class, description = @JsfDescription(description = "Client side callback execute when ajax requests returns with error response.")),
            @JsfAttribute(name = "onsuccess", type = String.class, description = @JsfDescription(description = "Client side callback execute before dom is updated.")),
            @JsfAttribute(name = "immediate", type = boolean.class, defaultValue="false", description = @JsfDescription(description = "Boolean value that determines the phaseId to execute listener. Default is false meaning \"Invoke Application\" phase, when true phase is \"Apply Request Values\".")),
            @JsfAttribute(name = "global", type = boolean.class, defaultValue="false", description = @JsfDescription(description = "Global ajax requests are listened by ajaxStatus component, setting global to false will not trigger ajaxStatus.")),
            @JsfAttribute(name = "disabled", type = boolean.class, defaultValue="false", description = @JsfDescription(description = "Disables ajax behavior.")),
            @JsfAttribute(name = "event", type = String.class, description = @JsfDescription(description = "Client side event to trigger ajax request. Default value is defined by parent ClientBehaviorHolder component the behavior is attached to.")),
            @JsfAttribute(name = "partialSubmit", type = boolean.class, defaultValue = "false", description = @JsfDescription(description = "When enabled, only values related to partially processed components would be serialized for ajax instead of whole form."))
        }
)
public class AjaxBehavior extends AjaxBehaviorBase implements BaseBehavior{
            
    private boolean partialSubmitSet = false;
    
    @Override
    public void setPartialSubmit(boolean partialSubmit) {
        super.setPartialSubmit(partialSubmit);
        this.partialSubmitSet = true;
    }
    
    public boolean isPartialSubmitSet() {
        return this.partialSubmitSet || (this.getValueExpression("partialSubmit") != null);
    }
    
    public boolean isImmediateSet() {
        return ((immediate != null) || (getValueExpression("immediate") != null));
    }
    
    public void addAjaxBehaviorListener(AjaxBehaviorListener listener) {
        addBehaviorListener(listener);
    }

    public void removeAjaxBehaviorListener(AjaxBehaviorListener listener) {
        removeBehaviorListener(listener);
    }
}
