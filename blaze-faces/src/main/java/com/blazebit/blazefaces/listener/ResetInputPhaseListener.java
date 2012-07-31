/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.listener;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author Christian Beikov
 * @since 0.1.2
 */
public class ResetInputPhaseListener implements PhaseListener {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ResetInputPhaseListener.class.getName());

    @Override
    public void afterPhase(PhaseEvent pe) {
        if (pe.getFacesContext().getPartialViewContext().isAjaxRequest()) {
            Collection<String> renderIds = pe.getFacesContext().getPartialViewContext().getRenderIds();
            UIViewRoot view = pe.getFacesContext().getViewRoot();

            for (String renderId : renderIds) {
                UIComponent comp = findComponent(view, renderId);

                if (comp != null) {
                    recursiveReset(comp);
                } else {
                    log.log(Level.WARNING, "Could not find component with id '" + renderId + "' in view '" + view.getViewId() + "'");
                }
            }
        }
    }

    /**
     * Originally taken from Mojarra implementation of UIComponentBase with the
     * addition of special checking when the NamingContainer is of the type UIForm.
     */
    private UIComponent findComponent(UIComponent comp, String expr) {
        if (expr == null) {
            throw new NullPointerException();
        }
        
        FacesContext ctx = FacesContext.getCurrentInstance();
        final char sepChar = UINamingContainer.getSeparatorChar(ctx);
        final String SEPARATOR_STRING = String.valueOf(sepChar);

        if (expr.length() == 0) {
            // if an empty value is provided, fail fast.
            throw new IllegalArgumentException("\"\"");
        }

        // Identify the base component from which we will perform our search
        UIComponent base = comp;
        if (expr.charAt(0) == sepChar) {
            // Absolute searches start at the root of the tree
            while (base.getParent() != null) {
                base = base.getParent();
            }
            // Treat remainder of the expression as relative
            expr = expr.substring(1);
        } else if (!(base instanceof NamingContainer)) {
            // Relative expressions start at the closest NamingContainer or root
            while (base.getParent() != null) {
                if (base instanceof NamingContainer) {
                    break;
                }
                base = base.getParent();
            }
        }

        // Evaluate the search expression (now guaranteed to be relative)
        UIComponent result = null;
        String[] segments = expr.split(SEPARATOR_STRING);
        for (int i = 0, length = (segments.length - 1);
                i < segments.length;
                i++, length--) {
            result = findComponent(base, segments[i], (i == 0));
            // the first element of the expression may match base.id
            // (vs. a child if of base)
            if (i == 0 && result == null
                    && segments[i].equals(base.getId())) {
                result = base;
            }
            if (result != null && (!(result instanceof NamingContainer)) && length > 0) {
                throw new IllegalArgumentException(segments[i]);
            }
            if (result == null) {
                break;
            }
            base = result;
        }

        // Return the final result of our search
        return (result);
    }

    private UIComponent findComponent(UIComponent base, String id, boolean checkId) {
        if (checkId && id.equals(base.getId())) {
            return base;
        }
        // Search through our facets and children
        UIComponent result = null;
        for (Iterator<UIComponent> i = base.getFacetsAndChildren(); i.hasNext();) {
            UIComponent kid = i.next();
            // Special handling for UIForm because of the attribute prependId
            if (!(kid instanceof NamingContainer) || (kid instanceof UIForm && !((UIForm) kid).isPrependId() && !id.equals(kid.getId()))) {
                if (checkId && id.equals(kid.getId())) {
                    result = kid;
                    break;
                }
                result = findComponent(kid, id, true);
                if (result != null) {
                    break;
                }
            } else if (id.equals(kid.getId())) {
                result = kid;
                break;
            }
        }
        return (result);

    }

    private void recursiveReset(UIComponent comp) {
        if (comp instanceof EditableValueHolder) {
            ((EditableValueHolder) comp).resetValue();
        }

        for (Iterator<UIComponent> i = comp.getFacetsAndChildren(); i.hasNext();) {
            recursiveReset(i.next());
        }
    }

    @Override
    public void beforePhase(PhaseEvent pe) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
