/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.button;

import java.util.Arrays;
import java.util.Collection;

import javax.faces.component.html.HtmlOutcomeTargetButton;
import javax.faces.context.FacesContext;

public class Button extends HtmlOutcomeTargetButton {

    public static final String COMPONENT_TYPE = "com.blazebit.blazefaces.component.Button";
    public static final String COMPONENT_FAMILY = "com.blazebit.blazefaces.component";
    private static final String DEFAULT_RENDERER = "com.blazebit.blazefaces.component.ButtonRenderer";

    public Button() {
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

    @Override
    public String getDefaultEventName() {
        return "action";
    }

    @Override
    public Collection<String> getEventNames() {
        return Arrays.asList("action");
    }

}
