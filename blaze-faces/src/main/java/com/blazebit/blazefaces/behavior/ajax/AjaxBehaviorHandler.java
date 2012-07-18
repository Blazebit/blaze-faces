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

    @SuppressWarnings("unused")
	private final TagAttribute event;
    private final TagAttribute process;
    private final TagAttribute update;
    private final TagAttribute onevent;
    private final TagAttribute onerror;
    private final TagAttribute disabled;
    private final TagAttribute immediate;
    private final TagAttribute listener;

    private final boolean wrapping;
    
    public AjaxBehaviorHandler(BehaviorConfig config) {
        super(config);
        this.event = this.getAttribute("event");
        this.process = this.getAttribute("process");
        this.update = this.getAttribute("update");
        this.onevent = this.getAttribute("onevent");
        this.onerror = this.getAttribute("onerror");
        this.disabled = this.getAttribute("disabled");
        this.immediate = this.getAttribute("immediate");
        this.listener = this.getAttribute("listener");

        this.wrapping = isWrapping();
    }

    @SuppressWarnings("rawtypes")
	@Override
    protected MetaRuleset createMetaRuleset(Class type) {
        MetaRuleset metaRuleset = super.createMetaRuleset(type);
        metaRuleset.addRule(new MethodRule("listener", null, new Class[0]));
        return metaRuleset;
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
        return ((this.nextHandler instanceof TagHandler) || 
                (this.nextHandler instanceof CompositeFaceletHandler));
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
            BeanInfo componentBeanInfo = (BeanInfo) parent.getAttributes().get(
                  UIComponent.BEANINFO_KEY);
            if (null == componentBeanInfo) {
                throw new TagException(
                      tag,
                      "Error: enclosing composite component does not have BeanInfo attribute");
            }
            BeanDescriptor componentDescriptor = componentBeanInfo.getBeanDescriptor();
            if (null == componentDescriptor) {
                throw new TagException(
                      tag,
                      "Error: enclosing composite component BeanInfo does not have BeanDescriptor");
            }
            List<AttachedObjectTarget> targetList = (List<AttachedObjectTarget>)
                  componentDescriptor
                        .getValue(AttachedObjectTarget.ATTACHED_OBJECT_TARGETS_KEY);
            if (null == targetList && !tagApplied) {
                throw new TagException(
                      tag,
                      "Error: enclosing composite component does not support behavior events");
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
                getAttachedObjectHandlers(parent, true).add(this);
            } else {
                if (!tagApplied) {
                    throw new TagException(
                            tag,
                            "Error: enclosing composite component does not support event "
                            + eventName);
                }
            }
        } else if (parent instanceof ClientBehaviorHolder) {
            applyAttachedObject(ctx, parent, eventName);
        } else {
            throw new TagException(this.tag,
                                   "Unable to attach <f:ajax> to non-ClientBehaviorHolder parent");
        }

    }
    
    @SuppressWarnings("unchecked")
	public static List<AttachedObjectHandler> getAttachedObjectHandlers(UIComponent component,
                                                                        boolean create) {
        Map<String, Object> attrs = component.getAttributes();
        List<AttachedObjectHandler> result = (List<AttachedObjectHandler>)
              attrs.get("javax.faces.RetargetableHandlers");

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
    
    private void applyAttachedObject(FaceletContext ctx,
                                     UIComponent parent,
                                     String eventName) {
        ClientBehaviorHolder bHolder = (ClientBehaviorHolder) parent;

        if (null == eventName) {
            eventName = bHolder.getDefaultEventName();
            if (null == eventName) {
                throw new TagException(this.tag,
                    "Event attribute could not be determined: "
                        + eventName);
            }
        } else {
            Collection<String> eventNames = bHolder.getEventNames();
            if (!eventNames.contains(eventName)) {
                throw new TagException(this.tag, 
                    getUnsupportedEventMessage(eventName, eventNames, parent));
            }
        }

        AjaxBehavior ajaxBehavior = createAjaxBehavior(ctx, eventName);
        bHolder.addClientBehavior(eventName, ajaxBehavior);
//        installAjaxResourceIfNecessary();
    }

    // Construct our AjaxBehavior from tag parameters.
    private AjaxBehavior createAjaxBehavior(FaceletContext ctx, String eventName) {
        Application application = ctx.getFacesContext().getApplication();
        AjaxBehavior behavior = (AjaxBehavior)application.createBehavior(
                                                  AjaxBehavior.BEHAVIOR_ID);

        setBehaviorAttribute(ctx, behavior, this.onevent, String.class);
        setBehaviorAttribute(ctx, behavior, this.onerror, String.class);
        setBehaviorAttribute(ctx, behavior, this.disabled, Boolean.class);
        setBehaviorAttribute(ctx, behavior, this.immediate, Boolean.class);
        setBehaviorAttribute(ctx, behavior, this.process, Object.class);
        setBehaviorAttribute(ctx, behavior, this.update, Object.class);

        if (null != listener) {
            behavior.addAjaxBehaviorListener(new AjaxBehaviorListenerImpl(
                this.listener.getMethodExpression(ctx, Object.class, new Class[] { AjaxBehaviorEvent.class }),
                this.listener.getMethodExpression(ctx, Object.class, new Class[] { })));
        }

        return behavior;
    }

    // Sets the value from the TagAttribute on the behavior
    @SuppressWarnings("rawtypes")
	private void setBehaviorAttribute(FaceletContext ctx,
                                      AjaxBehavior behavior,
                                      TagAttribute attr,
                                      Class type) {

        if (attr != null) {
            behavior.setValueExpression(attr.getLocalName(),
                                        attr.getValueExpression(ctx, type));
        }    
    }

    // Only install the Ajax resource if it doesn't exist.
    // The resource component will be installed with the target "head".
    //
//    private void installAjaxResourceIfNecessary() {
//
//        FacesContext context = FacesContext.getCurrentInstance();
//        if (RenderKitUtils.hasScriptBeenRendered(context)) {
//            // Already included, return
//            return;
//        }
//
//        final String name = "jsf.js";
//        final String library = "javax.faces";
//
//        if (RenderKitUtils.hasResourceBeenInstalled(context, name, library)) {
//            RenderKitUtils.setScriptAsRendered(context);
//            return;
//        }
//        UIOutput output = new UIOutput();
//        output.setRendererType("javax.faces.resource.Script");
//        output.getAttributes().put("name", name);
//        output.getAttributes().put("library", library);
//        context.getViewRoot().addComponentResource(context, output, "head");
//
//        // Set the context to record script as included
//        RenderKitUtils.setScriptAsRendered(context);
//
//    }

    // Returns an error message for the case where the <f:ajax> event
    // attribute specified an unknown/unsupported event.
    private String getUnsupportedEventMessage(String             eventName,
                                              Collection<String> eventNames,
                                              UIComponent        parent) {
        StringBuilder builder = new StringBuilder(100);
        builder.append("'");
        builder.append(eventName);
        builder.append("' is not a supported event for ");
        builder.append(parent.getClass().getSimpleName());
        builder.append(".  Please specify one of these supported event names: ");

        // Might as well sort the event names for a cleaner error message.
        Collection<String> sortedEventNames = new TreeSet<String>(eventNames);
        Iterator<String> iter = sortedEventNames.iterator();

        boolean hasNext = iter.hasNext();
        while (hasNext) {
            builder.append(iter.next());

            hasNext = iter.hasNext();

            if (hasNext) {
                builder.append(", ");
            }
        }

        builder.append(".");

        return builder.toString();
    }
}

class AjaxBehaviorListenerImpl implements AjaxBehaviorListener, Serializable {
    private static final long serialVersionUID = -6056525197409773897L;

    private MethodExpression oneArgListener;
    private MethodExpression noArgListener;

    // Necessary for state saving
    public AjaxBehaviorListenerImpl() {}

    public AjaxBehaviorListenerImpl(MethodExpression oneArg, MethodExpression noArg) {
        this.oneArgListener = oneArg;
        this.noArgListener = noArg;
    }

    public void processAjaxBehavior(AjaxBehaviorEvent event) throws AbortProcessingException {
        final ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        try{
            noArgListener.invoke(elContext, new Object[]{});
        } catch (MethodNotFoundException mnfe) {
            // Attempt to call public void method(AjaxBehaviorEvent event)
            oneArgListener.invoke(elContext, new Object[]{event});
        }
    }
}