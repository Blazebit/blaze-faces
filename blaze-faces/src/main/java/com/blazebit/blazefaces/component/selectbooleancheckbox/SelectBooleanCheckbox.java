/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.selectbooleancheckbox;

import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.FacesContext;

public class SelectBooleanCheckbox extends HtmlSelectBooleanCheckbox implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "com.blazebit.blazefaces.component.SelectBooleanCheckbox";
    public static final String COMPONENT_FAMILY = "com.blazebit.blazefaces.component";
    private static final String DEFAULT_RENDERER = "com.blazebit.blazefaces.component.SelectBooleanCheckboxRenderer";

    public SelectBooleanCheckbox() {
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
