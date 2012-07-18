/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.context;

import java.util.Collection;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextWrapper;
import com.blazebit.blazefaces.util.FeatureDetectionUtil;

public class BlazePartialViewContext extends PartialViewContextWrapper {

    private PartialViewContext wrapped;
    private PartialResponseWriter writer = null;

    public BlazePartialViewContext(PartialViewContext wrapped) {
        this.wrapped = wrapped;
        
        if(isAjaxRequest()) {
            BlazeRequestContext.init();
            FeatureDetectionUtil.retrieveFeatures(FacesContext.getCurrentInstance());
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
    public Collection<String> getRenderIds() {
        RequestContext requestContext = RequestContext.getCurrentInstance();

        if (requestContext == null || requestContext.getPartialUpdateTargets().isEmpty()) {
            return getWrapped().getRenderIds();
        } else {
            requestContext.addPartialUpdateTargets(getWrapped().getRenderIds());

            return requestContext.getPartialUpdateTargets();
        }
    }
}