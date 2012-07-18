/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.commandlink;

import com.blazebit.blazefaces.component.Command;
import javax.faces.context.FacesContext;
import javax.faces.component.html.HtmlCommandLink;

public class CommandLink extends HtmlCommandLink implements Command {


    public static final String COMPONENT_TYPE = "com.blazebit.blazefaces.component.CommandLink";
    public static final String COMPONENT_FAMILY = "com.blazebit.blazefaces.component";
    private static final String DEFAULT_RENDERER = "com.blazebit.blazefaces.component.CommandLinkRenderer";

    public CommandLink() {
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
