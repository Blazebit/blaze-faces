/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.behavior.ajax;

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
import com.blazebit.blazefaces.util.ComponentUtil;

/**
 *
 * @author Christian Beikov
 */
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
        if (ajaxBehavior.isDisabled()) {
            return null;
        }

        FacesContext fc = behaviorContext.getFacesContext();
        UIComponent component = behaviorContext.getComponent();
        String clientId = component.getClientId(fc);
        String source = behaviorContext.getSourceId();
        if (source == null) {
            source = component.getClientId(fc);
        }

        StringBuilder req = new StringBuilder();
        req.append("BlazeJS.Ajax.send(");

        //source
        req.append("{source:").append("'").append(source).append("'");

        //process
        String process = ajaxBehavior.getProcess() != null && !ajaxBehavior.getProcess().isEmpty() ? ComponentUtil.findClientIds(fc, component, ajaxBehavior.getProcess()) : clientId;
        req.append(",process:'").append(process).append("'");

        //update
        if (ajaxBehavior.getUpdate() != null) {
            req.append(",update:'").append(ComponentUtil.findClientIds(fc, component, ajaxBehavior.getUpdate())).append("'");
        }

        //behavior event
        req.append(",event:'").append(behaviorContext.getEventName()).append("'");

        //async
        if (ajaxBehavior.isAsync()) {
            req.append(",async:").append(Boolean.toString(ajaxBehavior.isAsync()));
        }

        //global
        if (!ajaxBehavior.isGlobal()) {
            req.append(",global:").append(Boolean.toString(ajaxBehavior.isGlobal()));
        }

        //callbacks
        if (ajaxBehavior.getOnstart() != null) {
            req.append(",onstart:function(xhr){").append(ajaxBehavior.getOnstart()).append(";}");
        }
        if (ajaxBehavior.getOnerror() != null) {
            req.append(",onerror:function(xhr, status, error){").append(ajaxBehavior.getOnerror()).append(";}");
        }
        if (ajaxBehavior.getOnsuccess() != null) {
            req.append(",onsuccess:function(data, status, xhr){").append(ajaxBehavior.getOnsuccess()).append(";}");
        }
        if (ajaxBehavior.getOncomplete() != null) {
            req.append(",oncomplete:function(xhr, status, args){").append(ajaxBehavior.getOncomplete()).append(";}");
        }

        //params
        boolean paramWritten = false;

        for (UIComponent child : component.getChildren()) {
            if (child instanceof UIParameter) {
                UIParameter parameter = (UIParameter) child;

                if (!paramWritten) {
                    paramWritten = true;
                    req.append(",params:{");
                } else {
                    req.append(",");
                }

                req.append("'").append(parameter.getName()).append("':'").append(parameter.getValue()).append("'");
            }
        }

        if (paramWritten) {
            req.append("}");
        }


        req.append("}); return false;");
//        req.append("}, arguments[1]);");

        return req.toString();
//        return RendererUtil.getEventHandlerScript(fc, source, behaviorContext.getEventName(), req.toString());
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
