/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.selectmanymenu;

import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.html.HtmlSelectManyMenu;
import javax.faces.context.FacesContext;

public class SelectManyMenu extends HtmlSelectManyMenu implements ClientBehaviorHolder {


    public static final String COMPONENT_TYPE = "com.blazebit.blazefaces.component.SelectManyMenu";
    public static final String COMPONENT_FAMILY = "com.blazebit.blazefaces.component";
    private static final String DEFAULT_RENDERER = "com.blazebit.blazefaces.component.SelectManyMenu";

    public SelectManyMenu() {
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
