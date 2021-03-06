/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.context;

import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextFactory;

import com.blazebit.blazefaces.apt.JsfPartialViewContextFactory;

@JsfPartialViewContextFactory
public class BlazePartialViewContextFactory extends PartialViewContextFactory {

    private PartialViewContextFactory parent;
    
    public BlazePartialViewContextFactory(PartialViewContextFactory parent) {
        this.parent = parent;
    }

    @Override
    public PartialViewContextFactory getWrapped() {
        return this.parent;
    }

    @Override
    public PartialViewContext getPartialViewContext(FacesContext fc) {
        PartialViewContext parentContext = getWrapped().getPartialViewContext(fc);
        
        return new BlazePartialViewContext(parentContext);
    }
}