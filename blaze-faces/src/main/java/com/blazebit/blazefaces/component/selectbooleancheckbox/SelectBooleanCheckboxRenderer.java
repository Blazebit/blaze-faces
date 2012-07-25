/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.selectbooleancheckbox;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.blazebit.blazefaces.renderkit.InputRenderer;
import com.blazebit.blazefaces.util.ComponentUtils;
import com.blazebit.blazefaces.util.HTML5;
import com.blazebit.blazefaces.util.RendererUtils;

public class SelectBooleanCheckboxRenderer extends InputRenderer {
    
    @Override
    public void decode(FacesContext context, UIComponent component) {
        SelectBooleanCheckbox comp = (SelectBooleanCheckbox) component;
        
        if(ComponentUtils.componentIsDisabledOrReadonly(component))
            return;
        
        decodeBehaviors(context, comp);

        String clientId = comp.getClientId(context);
        String submittedValue = (String) context.getExternalContext().getRequestParameterMap().get(clientId);

        if(submittedValue != null && submittedValue.equalsIgnoreCase("on")) {
            comp.setSubmittedValue("true");
        }else{
            comp.setSubmittedValue("false");
        }
    }
    
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        SelectBooleanCheckbox comp = (SelectBooleanCheckbox) component;
        String clientId = component.getClientId(context);
        Boolean checked = (Boolean) comp.getValue();
        boolean disabled = comp.isDisabled();
        
        writer.startElement("input", component);
        RendererUtils.encodeAttribute(writer, "id", clientId, null);
        RendererUtils.encodeAttribute(writer, "name", clientId, null);
        RendererUtils.encodeAttribute(writer, "type", "checkbox", null);
        RendererUtils.encodeAttribute(writer, "class", component.getAttributes().get("styleClass"), null);
        
        if(checked)
            RendererUtils.encodeAttribute(writer, "checked", "checked", null);
        if(disabled)
            RendererUtils.encodeAttribute(writer, "disabled", "disabled", null);
        
        renderPassThruAttributes(context, component, HTML5.COMMON_ATTRIBUTES);
        writer.endElement("input");
        encodeBehaviors(context, (ClientBehaviorHolder) component);
    }
}
