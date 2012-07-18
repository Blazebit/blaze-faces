/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.selectmanycheckbox;

import javax.faces.context.FacesContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.html.HtmlSelectManyCheckbox;

public class SelectManyCheckbox extends HtmlSelectManyCheckbox implements ClientBehaviorHolder {


    public static final String COMPONENT_TYPE = "com.blazebit.blazefaces.component.SelectManyCheckbox";
    public static final String COMPONENT_FAMILY = "com.blazebit.blazefaces.component";
    private static final String DEFAULT_RENDERER = "com.blazebit.blazefaces.component.SelectManyCheckboxRenderer";

    public SelectManyCheckbox() {
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
