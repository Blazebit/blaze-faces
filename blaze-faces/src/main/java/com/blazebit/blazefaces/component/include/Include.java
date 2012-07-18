/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.include;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This component allows including facelets dynamically. It is using
 * preserved FaceletContext to build component trees based on the view path.
 * This FaceletContext is enriched with attributes passed to the
 * dynamicInclude tag.
 *
 * Initially subtrees are built in the rendering phase. Then on postback
 * request they are rebuilt in restoreView phase.
 */
public class Include extends UIComponentBase {

    private static final Logger log = LoggerFactory.getLogger(Include.class);
    private static final String SRC_ATTRIBUTE = "src";
    
    public static final String COMPONENT_FAMILY = "com.blazebit.blazefaces.component";
    public static final String COMPONENT_TYPE = "com.blazebit.blazefaces.component.Include";
    
    private Map<String, ValueExpression> valueExpressions = Collections.emptyMap();
    private List<String> renderedSources = new ArrayList<String>();
    private List<String> restoredSources = Collections.emptyList();
    // for ajax requests this will stay false
    private boolean wasRendered = false;
    private boolean wasPurged = false;
    private FaceletContext faceletContext;
    private String src;

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public void addValueExpression(String key, ValueExpression valueExpression) {
        if (valueExpressions == Collections.EMPTY_MAP) {
            valueExpressions = new HashMap<String, ValueExpression>();
        }
        valueExpressions.put(key, valueExpression);
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        wasRendered = true;

        if (isRendered()) {
            String localSrc = getSrc();

            if (!renderedSources.contains(localSrc)) {
                // As soon as we detect the first inconsistency between restored and
                // rendered views, we purge restored views. Otherwise we reuse restored
                // view and mark is as rendered.

                if (restoredSources.indexOf(localSrc) == renderedSources.size()) {
                    renderedSources.add(localSrc);
                } else if (!wasPurged) {
                    purgeRestoredChildren(context);
                }
            }

            // The hasChild() hopefully fixes the strange rendering behavior
            // the created children from the first request seem to disappear?
            if (!renderedSources.contains(localSrc)) {
                buildView(context, localSrc);
                renderedSources.add(localSrc);
            }else if(!hasChild(context, localSrc)) {
                buildView(context, localSrc);
            }
        }
    }

    /**
     * Remove all restored views except those marked as rendered.
     */
    private void purgeRestoredChildren(FacesContext context) {
        List<UIComponent> toRemove = new ArrayList<UIComponent>();
        for (UIComponent c : getChildren()) {
            if (!renderedSources.contains(((DynamicIncludeComponent) c).src)) {
                toRemove.add(c);
            }
        }

        this.getChildren().removeAll(toRemove);
        this.restoredSources = Collections.emptyList();
        this.wasPurged = true;
    }

    private void buildView(FacesContext context, String src) {
        try {
            log.debug("Building view: " + src);
            DynamicIncludeComponent panel = new DynamicIncludeComponent();
            int index = findFreeIndex();
            panel.src = src;
            panel.index = index;
            panel.setId(getId() + index);
            this.getChildren().add(index, panel);
            setAttributesInFaceletContext();
            faceletContext.includeFacelet(panel, src);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
        }
    }

    private int findFreeIndex() {
        for (int i = 0; i <= this.getChildCount(); i++) {
            boolean free = true;
            for (UIComponent c : this.getChildren()) {
                if (i == ((DynamicIncludeComponent) c).index) {
                    free = false;
                }
            }
            if (free) {
                return i;
            }
        }
        throw new IllegalStateException("Impossible! Could not find available index.");
    }

    /**
     * FaceletContext is used during creation of components from a facelet
     * template. If these components have attributes with EL expressions
     * referring to what's in dynamicInclude attributes, we have to make
     * them available in FaceletContext.
     */
    private void setAttributesInFaceletContext() {
        for (Map.Entry<String, ValueExpression> var : valueExpressions.entrySet()) {
            faceletContext.getVariableMapper().setVariable(var.getKey(), var.getValue());
        }
    }

    @Override
    public Object saveState(FacesContext context) {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = wasRendered ? renderedSources : restoredSources;
        return values;
    }

    /**
     * We rebuild subviews here. The children state will be restored
     * by StateManagementStrategy because it first restores state of a node
     * then processes its children.
     */
    @SuppressWarnings("unchecked")
	@Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        this.restoredSources = (List<String>) values[1];   
        
        /** 
         * @todo: Normally there should be no need to rebuild the subviews, because they should be restored
         * Needs further diagnosis, just like that the rendered attribute does not work as expected and 
         * this component can only be used within UIRepeat. UIInclude is preferred instead of this component
         * for static includation.
         */
        for (String localSrc : this.restoredSources)
            buildView(context, localSrc);
    }

    /* getters and setters */
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSrc() {
        if (src == null) {
            ValueExpression ve = getValueExpression(SRC_ATTRIBUTE);
            // Fast fix for poorly diagnosed error
            if (ve == null) {
                ve = valueExpressions.get(SRC_ATTRIBUTE);
            }
            return (String) ve.getValue(getFacesContext().getELContext());
        }

        return src;
    }

    public void setFaceletContext(FaceletContext faceletContext) {
        this.faceletContext = faceletContext;
    }

    private boolean hasChild(FacesContext context, String localSrc) {
        for (UIComponent c : getChildren()) {
            if (localSrc.equals(((DynamicIncludeComponent) c).src)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * This serves as root for possible subviews included by DynamicInclude.
     */
    public static class DynamicIncludeComponent extends UIComponentBase implements NamingContainer, Serializable {

		private static final long serialVersionUID = 1L;
		
		private String src;
        private int index;

        @Override
        public String getFamily() {
            return COMPONENT_FAMILY;
        }

        @Override
        public boolean isRendered() {
            return src.equals(((Include) getParent()).getSrc());
        }

        @Override
        public void restoreState(FacesContext context, Object state) {
            Object values[] = (Object[]) state;
            super.restoreState(context, values[0]);
            this.src = (String) values[1];
            this.index = (Integer) values[2];
        }

        @Override
        public Object saveState(FacesContext context) {
            Object values[] = new Object[3];
            values[0] = super.saveState(context);
            values[1] = src;
            values[2] = index;
            return values;
        }
    }
}
