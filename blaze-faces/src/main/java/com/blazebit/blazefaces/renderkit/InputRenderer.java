/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.renderkit;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import com.blazebit.blazefaces.util.ComponentUtil;

public class InputRenderer extends OutputRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        UIInput comp = (UIInput) component;
        
        if(ComponentUtil.componentIsDisabledOrReadonly(component))
            return;
        
        decodeBehaviors(context, comp);

        String clientId = comp.getClientId(context);
        String submittedValue = (String) context.getExternalContext().getRequestParameterMap().get(clientId);

        if(submittedValue != null) {
            comp.setSubmittedValue(submittedValue);
        }
    }
    
}
