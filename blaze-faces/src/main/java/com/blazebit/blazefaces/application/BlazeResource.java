/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.application;

import com.blazebit.blazefaces.util.Constants;
import javax.faces.application.Resource;
import javax.faces.application.ResourceWrapper;

public class BlazeResource extends ResourceWrapper {

    private Resource resource;

    public BlazeResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public Resource getWrapped() {
        return this.resource;
    }

    @Override
    public String getRequestPath() {
        return super.getRequestPath() + "&amp;v=" + Constants.VERSION;
    }

    @Override
    public String getContentType() {
        return getWrapped().getContentType();
    }

    @Override
    public String getLibraryName() {
        return getWrapped().getLibraryName();
    }

    @Override
    public String getResourceName() {
        return getWrapped().getResourceName();
    }

    @Override
    public void setContentType(String contentType) {
        getWrapped().setContentType(contentType);
    }

    @Override
    public void setLibraryName(String libraryName) {
        getWrapped().setLibraryName(libraryName);
    }

    @Override
    public void setResourceName(String resourceName) {
        getWrapped().setResourceName(resourceName);
    }

    @Override
    public String toString() {
        return getWrapped().toString();
    }
}