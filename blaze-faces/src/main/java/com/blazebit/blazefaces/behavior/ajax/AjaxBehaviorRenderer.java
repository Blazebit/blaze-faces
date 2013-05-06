/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.behavior.ajax;

import com.blazebit.blazefaces.apt.JsfBehaviorRenderer;
import com.blazebit.blazefaces.util.AjaxRequestBuilder;
import com.blazebit.blazefaces.util.RendererUtils;

import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.PhaseId;
import javax.faces.render.ClientBehaviorRenderer;

import com.blazebit.blazefaces.util.ComponentUtils;

/**
 *
 * @author Christian Beikov
 */
@JsfBehaviorRenderer
public class AjaxBehaviorRenderer extends ClientBehaviorRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component, ClientBehavior behavior) {
        AjaxBehavior ajaxBehavior = (AjaxBehavior) behavior;

        if (!ajaxBehavior.isDisabled()) {
            AjaxBehaviorEvent event = new AjaxBehaviorEvent(component, behavior);
            PhaseId phaseId = isImmediate(component, ajaxBehavior) ? PhaseId.APPLY_REQUEST_VALUES : PhaseId.INVOKE_APPLICATION;
            event.setPhaseId(phaseId);
            component.queueEvent(event);
        }
    }

    @Override
    public String getScript(ClientBehaviorContext behaviorContext, ClientBehavior behavior) {
        AjaxBehavior ajaxBehavior = (AjaxBehavior) behavior;
        
        if(ajaxBehavior.isDisabled()) {
            return null;
        }
        
        FacesContext context = behaviorContext.getFacesContext();
        UIComponent component = behaviorContext.getComponent();
        String source = behaviorContext.getSourceId();
        String process = ajaxBehavior.getProcess();
        
        if(process == null) {
            process = "@this";
        }

        AjaxRequestBuilder builder = new AjaxRequestBuilder();
        
        String request = builder.source(context, component, source)
                        .event(behaviorContext.getEventName())
                        .process(context, component, process)
                        .update(context, component, ajaxBehavior.getUpdate())
                        .async(ajaxBehavior.isAsync())
                        .global(ajaxBehavior.isGlobal())
                        .partialSubmit(ajaxBehavior.isPartialSubmit(), ajaxBehavior.isPartialSubmitSet())
                        .onstart(ajaxBehavior.getOnstart())
                        .onerror(ajaxBehavior.getOnerror())
                        .onsuccess(ajaxBehavior.getOnsuccess())
                        .oncomplete(ajaxBehavior.getOncomplete())
                        .params(component)
                        .preventDefault()
                        .buildBehavior();
        	
        return request.toString();
//        return RendererUtils.getEventHandlerScript(context, source, behaviorContext.getEventName(), request.toString());
    }

    private boolean isImmediate(UIComponent component, AjaxBehavior ajaxBehavior) {
        boolean immediate = false;

        if (ajaxBehavior.isImmediateSet()) {
            immediate = ajaxBehavior.isImmediate();
        } else if (component instanceof EditableValueHolder) {
            immediate = ((EditableValueHolder) component).isImmediate();
        } else if (component instanceof ActionSource) {
            immediate = ((ActionSource) component).isImmediate();
        }

        return immediate;
    }
}
