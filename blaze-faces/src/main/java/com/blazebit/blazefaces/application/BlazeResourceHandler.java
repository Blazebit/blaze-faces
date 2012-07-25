/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.application;

import com.blazebit.blazefaces.util.Constants;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;

public class BlazeResourceHandler extends ResourceHandlerWrapper {

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

        if(resource != null && libraryName != null && libraryName.equalsIgnoreCase(Constants.LIBRARY)) {
            return new BlazeResource(resource);
        } else {
            return resource;
        }
    }
}