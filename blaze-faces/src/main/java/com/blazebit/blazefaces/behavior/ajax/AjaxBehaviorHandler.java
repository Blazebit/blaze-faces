/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.behavior.ajax;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.MethodNotFoundException;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;
import javax.faces.view.AttachedObjectHandler;
import javax.faces.view.AttachedObjectTarget;
import javax.faces.view.BehaviorHolderAttachedObjectTarget;
import javax.faces.view.facelets.BehaviorConfig;
import javax.faces.view.facelets.BehaviorHandler;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.CompositeFaceletHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;

import com.blazebit.blazefaces.facelets.MethodRule;

/**
 *
 * @author Christian Beikov
 */
public class AjaxBehaviorHandler extends BehaviorHandler {

    private final TagAttribute event;
    private final TagAttribute process;
    private final TagAttribute update;
    private final TagAttribute onstart;
    private final TagAttribute onerror;
    private final TagAttribute onsuccess;
    private final TagAttribute oncomplete;
    private final TagAttribute disabled;
    private final TagAttribute immediate;
    private final TagAttribute listener;
    private final TagAttribute global;
    private final TagAttribute async;
    private final TagAttribute partialSubmit;
    private final boolean wrapping;

    public AjaxBehaviorHandler(BehaviorConfig config) {
        super(config);
        this.event = this.getAttribute("event");
        this.process = this.getAttribute("process");
        this.update = this.getAttribute("update");
        this.onstart = this.getAttribute("onstart");
        this.onerror = this.getAttribute("onerror");
        this.onsuccess = this.getAttribute("onsuccess");
        this.oncomplete = this.getAttribute("oncomplete");
        this.disabled = this.getAttribute("disabled");
        this.immediate = this.getAttribute("immediate");
        this.listener = this.getAttribute("listener");
        this.global = this.getAttribute("global");
        this.async = this.getAttribute("async");
        this.partialSubmit = this.getAttribute("partialSubmit");

        this.wrapping = isWrapping();
    }

    @Override
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        String eventName = getEventName();

        if (!this.wrapping) {
            applyNested(ctx, parent, eventName);
        }
    }

    // Tests whether the <f:ajax> is wrapping other tags.
    private boolean isWrapping() {

        // Would be nice if there was some easy way to determine whether
        // we are a leaf handler.  However, even leaf handlers have a
        // non-null nextHandler - the CompilationUnit.LEAF instance.
        // We assume that if we've got a TagHandler or CompositeFaceletHandler
        // as our nextHandler, we are not a leaf.
        return ((this.nextHandler instanceof TagHandler)
                || (this.nextHandler instanceof CompositeFaceletHandler));
    }

    // Applies a nested AjaxHandler by adding the AjaxBehavior to the
    // parent component.
    @SuppressWarnings("unchecked")
    private void applyNested(FaceletContext ctx,
            UIComponent parent,
            String eventName) {

        if (!ComponentHandler.isNew(parent)) {
            return;
        }

        // Composite component case
        if (UIComponent.isCompositeComponent(parent)) {
            // Check composite component event name:
            boolean tagApplied = false;
            if (parent instanceof ClientBehaviorHolder) {
                applyAttachedObject(ctx, parent, eventName);  // error here will propagate up
                tagApplied = true;
            }

            BeanInfo componentBeanInfo = (BeanInfo) parent.getAttributes().get(UIComponent.BEANINFO_KEY);
            if (null == componentBeanInfo) {
                throw new TagException(tag, "Composite component does not have BeanInfo attribute");
            }

            BeanDescriptor componentDescriptor = componentBeanInfo.getBeanDescriptor();
            if (null == componentDescriptor) {
                throw new TagException(tag, "Composite component BeanInfo does not have BeanDescriptor");
            }

            List<AttachedObjectTarget> targetList = (List<AttachedObjectTarget>) componentDescriptor.getValue(AttachedObjectTarget.ATTACHED_OBJECT_TARGETS_KEY);
            if (null == targetList && !tagApplied) {
                throw new TagException(tag, "Composite component does not support behavior events");
            }

            boolean supportedEvent = false;
            for (AttachedObjectTarget target : targetList) {
                if (target instanceof BehaviorHolderAttachedObjectTarget) {
                    BehaviorHolderAttachedObjectTarget behaviorTarget = (BehaviorHolderAttachedObjectTarget) target;
                    if ((null != eventName && eventName.equals(behaviorTarget.getName()))
                            || (null == eventName && behaviorTarget.isDefaultEvent())) {
                        supportedEvent = true;
                        break;
                    }
                }
            }
            if (supportedEvent) {
                try {
                    // Special handling for myfaces
                    Class<?> faceletCompositionContextClass = Class.forName("org.apache.myfaces.view.facelets.FaceletCompositionContext");
                    Object mctx = faceletCompositionContextClass.getMethod("getCurrentInstance", FaceletContext.class).invoke(null, ctx);
                    faceletCompositionContextClass.getMethod("addAttachedObjectHandler", UIComponent.class, AttachedObjectHandler.class).invoke(mctx, parent, this);
                } catch (Exception ex) {
                    // Fall back to mojarra solution
                    getAttachedObjectHandlers(parent, true).add(this);
                }
            } else {
                if (!tagApplied) {
                    throw new TagException(tag, "Composite component does not support event " + eventName);
                }
            }
        } else if (parent instanceof ClientBehaviorHolder) {
            applyAttachedObject(ctx, parent, eventName);
        } else {
            throw new TagException(this.tag, "Unable to attach <b:ajax> to non-ClientBehaviorHolder parent");
        }
    }

    @Override
    public String getEventName() {
        return (this.event != null) ? this.event.getValue() : null;
    }

    @SuppressWarnings("unchecked")
    public static List<AttachedObjectHandler> getAttachedObjectHandlers(UIComponent component, boolean create) {
        Map<String, Object> attrs = component.getAttributes();
        List<AttachedObjectHandler> result = (List<AttachedObjectHandler>) attrs.get("javax.faces.RetargetableHandlers");

        if (result == null) {
            if (create) {
                result = new ArrayList<AttachedObjectHandler>();
                attrs.put("javax.faces.RetargetableHandlers", result);
            } else {
                result = Collections.EMPTY_LIST;
            }
        }
        return result;

    }

//    public void applyAttachedObject(FacesContext context, UIComponent parent) {
//        FaceletContext ctx = (FaceletContext) context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
//        
//        applyAttachedObject(ctx, parent, getEventName());
//    }

//    public String getFor() {
//        return null;
//    }
    
    public void applyAttachedObject(FaceletContext context, UIComponent component, String eventName) {                
        ClientBehaviorHolder holder = (ClientBehaviorHolder) component;

        if(null == eventName) {
            eventName = holder.getDefaultEventName();
            if (null == eventName) {
                throw new TagException(this.tag, "Event attribute could not be determined: "  + eventName);
            }
        } else {
            Collection<String> eventNames = holder.getEventNames();
            if (!eventNames.contains(eventName)) {
                throw new TagException(this.tag,  "Event:" + eventName + " is not supported.");
            }
        }

        AjaxBehavior ajaxBehavior = createAjaxBehavior(context, eventName);
        holder.addClientBehavior(eventName, ajaxBehavior);
    }

    private AjaxBehavior createAjaxBehavior(FaceletContext ctx, String eventName) {
        Application application = ctx.getFacesContext().getApplication();
        AjaxBehavior behavior = (AjaxBehavior) application.createBehavior(AjaxBehavior.BEHAVIOR_ID);

        setBehaviorAttribute(ctx, behavior, this.process, String.class);
        setBehaviorAttribute(ctx, behavior, this.update, String.class);
        setBehaviorAttribute(ctx, behavior, this.onstart, String.class);
        setBehaviorAttribute(ctx, behavior, this.onerror, String.class);
        setBehaviorAttribute(ctx, behavior, this.onsuccess, String.class);
        setBehaviorAttribute(ctx, behavior, this.oncomplete, String.class);
        setBehaviorAttribute(ctx, behavior, this.disabled, Boolean.class);
        setBehaviorAttribute(ctx, behavior, this.immediate, Boolean.class);
        setBehaviorAttribute(ctx, behavior, this.global, Boolean.class);
        setBehaviorAttribute(ctx, behavior, this.async, Boolean.class);
        setBehaviorAttribute(ctx, behavior, this.partialSubmit, Boolean.class);
        setBehaviorAttribute(ctx, behavior, this.listener, MethodExpression.class);

        if (listener != null) {
            behavior.addAjaxBehaviorListener(new AjaxBehaviorListenerImpl(
                    this.listener.getMethodExpression(ctx, Object.class, new Class<?>[]{})));
        }

        return behavior;
    }

    private void setBehaviorAttribute(FaceletContext ctx, AjaxBehavior behavior, TagAttribute attr, Class<?> type) {
        if (attr != null) {
            behavior.setValueExpression(attr.getLocalName(), attr.getValueExpression(ctx, type));
        }
    }
}