/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.inputtext;

import javax.faces.context.FacesContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.html.HtmlInputText;

public class InputText extends HtmlInputText implements ClientBehaviorHolder {


    public static final String COMPONENT_TYPE = "com.blazebit.blazefaces.component.InputText";
    public static final String COMPONENT_FAMILY = "com.blazebit.blazefaces.component";
    private static final String DEFAULT_RENDERER = "com.blazebit.blazefaces.component.InputTextRenderer";

    public InputText() {
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
