/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.context;

import java.util.Collection;

import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextWrapper;

import com.blazebit.blazefaces.util.FeatureDetectionUtils;

public class BlazePartialViewContext extends PartialViewContextWrapper {

    private PartialViewContext wrapped;
    private PartialResponseWriter writer = null;

    public BlazePartialViewContext(PartialViewContext wrapped) {
        this.wrapped = wrapped;
        
        if(isAjaxRequest()) {
            new DefaultRequestContext();
            FeatureDetectionUtils.retrieveFeatures(FacesContext.getCurrentInstance());
        }
    }
    
    @Override
    public PartialViewContext getWrapped() {
        return this.wrapped;
    }

    @Override
    public void setPartialRequest(boolean value) {
        getWrapped().setPartialRequest(value);
    }

    @Override
    public PartialResponseWriter getPartialResponseWriter() {
        if (writer == null) {
            PartialResponseWriter parentWriter = getWrapped().getPartialResponseWriter();
            writer = new BlazePartialResponseWriter(parentWriter);
        }

        return writer;
    }

    @Override
    public boolean isAjaxRequest() {
        return getWrapped().isAjaxRequest()
                || FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("javax.faces.partial.ajax");
    }

    @Override
    public boolean isPartialRequest() {
        return getWrapped().isPartialRequest()
                || FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("javax.faces.partial.execute");
    }
}