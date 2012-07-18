/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.commandbutton;

import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;

import com.blazebit.blazefaces.component.Command;

public class CommandButton extends HtmlCommandButton implements Command {


    public static final String COMPONENT_TYPE = "com.blazebit.blazefaces.component.CommandButton";
    public static final String COMPONENT_FAMILY = "com.blazebit.blazefaces.component";
    private static final String DEFAULT_RENDERER = "com.blazebit.blazefaces.component.CommandButtonRenderer";

    public CommandButton() {
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
