/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.application;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;

public class BlazeResourceHandler extends ResourceHandlerWrapper {

    public final static String VERSION = "0.1";
    public final static String LIBRARY = "blazefaces";

    private ResourceHandler wrapped;

    public BlazeResourceHandler(ResourceHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ResourceHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public Resource createResource(String resourceName, String libraryName) {
        Resource resource = super.createResource(resourceName, libraryName);

        if(resource != null && libraryName != null && libraryName.equalsIgnoreCase(LIBRARY)) {
            return new BlazeResource(resource);
        } else {
            return resource;
        }
    }
}