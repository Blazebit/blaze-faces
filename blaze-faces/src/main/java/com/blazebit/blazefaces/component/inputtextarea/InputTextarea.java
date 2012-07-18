/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.inputtextarea;

import javax.faces.context.FacesContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.html.HtmlInputTextarea;

public class InputTextarea extends HtmlInputTextarea implements ClientBehaviorHolder {


    public static final String COMPONENT_TYPE = "com.blazebit.blazefaces.component.InputTextarea";
    public static final String COMPONENT_FAMILY = "com.blazebit.blazefaces.component";
    private static final String DEFAULT_RENDERER = "com.blazebit.blazefaces.component.InputTextareaRenderer";

    public InputTextarea() {
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
