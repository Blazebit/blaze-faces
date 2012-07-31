/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.context;

import java.io.IOException;
import java.util.Map;

import javax.faces.context.FacesContext;

/**
 *
 * @author RedShadow
 */
public interface BlazeEncoder {
    
    public void encode(FacesContext context, Map<String, Object> attributes) throws IOException;
}
