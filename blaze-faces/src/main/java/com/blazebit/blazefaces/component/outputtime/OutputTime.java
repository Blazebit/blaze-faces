/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.outputtime;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIOutput;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;


/**
 *
 * @author Christian Beikov
 */
@ResourceDependencies({
    @ResourceDependency(library="blazefaces", name="core/html5.js", target="head_lt_ie9"),
    @ResourceDependency(library="blazefaces", name="core/innershiv.js", target="head")
})
public class OutputTime extends UIOutput implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "com.blazebit.blazefaces.component.OutputTime";
    public static final String COMPONENT_FAMILY = "com.blazebit.blazefaces.component";
    private static final String DEFAULT_RENDERER = "com.blazebit.blazefaces.component.OutputTimeRenderer";
    
    public OutputTime() {
        setRendererType(DEFAULT_RENDERER);
        setConverter(getFacesContext().getApplication().createConverter(DateTimeConverter.class));
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
