/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.panelgroup;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIPanel;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;


/**
 *
 * @author Christian Beikov
 */
@ResourceDependencies({
    @ResourceDependency(library="blazefaces", name="core/html5.js", target="head_lt_ie9"),
    @ResourceDependency(library="blazefaces", name="core/innershiv.js", target="head")
})
public class PanelGroup extends UIPanel implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "com.blazebit.blazefaces.component.PanelGroup";
    public static final String COMPONENT_FAMILY = "com.blazebit.blazefaces.component";
    private static final String DEFAULT_RENDERER = "com.blazebit.blazefaces.component.PanelGroupRenderer";
    
    public PanelGroup() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }
}
