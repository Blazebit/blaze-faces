/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.selectmanycheckbox;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import com.blazebit.blazefaces.renderkit.SelectRenderer;
import com.blazebit.blazefaces.util.ComponentUtils;
import com.blazebit.blazefaces.util.HTML5;

public class SelectManyCheckboxRenderer extends SelectRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        SelectManyCheckbox comp = (SelectManyCheckbox) component;

        if (ComponentUtils.componentIsDisabledOrReadonly(component)) {
            return;
        }

        decodeBehaviors(context, comp);

        String clientId = comp.getClientId(context);
        String[] submittedValues = context.getExternalContext().getRequestParameterValuesMap().get(clientId);

        if (submittedValues != null) {
            comp.setSubmittedValue(submittedValues);
        } else {
            comp.setSubmittedValue(new String[0]);
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        SelectManyCheckbox comp = (SelectManyCheckbox) component;
        
        encodeSelectItems(context, comp);
        encodeBehaviors(context, (ClientBehaviorHolder) component);
    }
    
        protected void encodeOptionInput(FacesContext context, SelectManyCheckbox checkbox, String clientId, String containerClientId, boolean checked, boolean disabled, String label, String formattedValue) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        writer.startElement("div", null);
        writer.writeAttribute("class", "ui-checkbox-inputwrapper", null);

        writer.startElement("input", null);
        writer.writeAttribute("id", containerClientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("type", "checkbox", null);
        writer.writeAttribute("value", formattedValue, null);

        if(checked) writer.writeAttribute("checked", "checked", null);
        if(disabled) writer.writeAttribute("disabled", "disabled", null);
        if(checkbox.getOnchange() != null) writer.writeAttribute("onchange", checkbox.getOnchange(), null);
        renderPassThruAttributes(context, checkbox, HTML5.COMMON_ATTRIBUTES);

        writer.endElement("input");

        writer.endElement("div");
    }

    protected void encodeOptionLabel(FacesContext context, SelectManyCheckbox checkbox, String containerClientId, String label) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("label", null);
        writer.writeAttribute("for", containerClientId, null);
        writer.write(label);
        writer.endElement("label");
    }

    protected void encodeOptionOutput(FacesContext context, SelectManyCheckbox checkbox, boolean checked) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String styleClass = "ui-checkbox-box ui-widget ui-corner-all ui-checkbox-relative ui-state-default";
        styleClass = checked ? styleClass + " ui-state-active" : styleClass;

        String iconClass = "ui-checkbox-icon";
        iconClass = checked ? iconClass + " ui-icon ui-icon-check" : iconClass;

        writer.startElement("div", null);
        writer.writeAttribute("class", styleClass, null);

        writer.startElement("span", null);
        writer.writeAttribute("class", iconClass, null);
        writer.endElement("span");

        writer.endElement("div");
    }

    protected void encodeSelectItems(FacesContext context, SelectManyCheckbox checkbox) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        List<SelectItem> selectItems = getSelectItems(context, checkbox);
        Converter converter = getConverter(context, checkbox);
        Object value = checkbox.getValue();
        String layout = checkbox.getLayout();
        boolean pageDirection = layout != null && layout.equals("pageDirection");

        for(SelectItem selectItem : selectItems) {
            Object itemValue = selectItem.getValue();
            String itemLabel = selectItem.getLabel();

            if(pageDirection) {
                writer.startElement("tr", null);
            }

            encodeOption(context, checkbox, value, converter, itemLabel, itemValue);

            if(pageDirection) {
                writer.endElement("tr");
            }
        }
    }

    protected void encodeOption(FacesContext context, UIInput component, Object componentValue, Converter converter, String label, Object value) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        SelectManyCheckbox checkbox = (SelectManyCheckbox) component;
        String formattedValue = getOptionAsString(context, component, converter, value);
        String clientId = component.getClientId(context);
        String containerClientId = component.getContainerClientId(context);
        boolean checked = componentValue != null && ((List<?>) componentValue).contains(value);
        boolean disabled = checkbox.isDisabled();

        writer.startElement("td", null);

        String styleClass = "ui-checkbox ui-widget";
        if(disabled) {
            styleClass += " ui-state-disabled";
        }

        writer.startElement("div", null);
        writer.writeAttribute("class", styleClass, null);

        encodeOptionInput(context, checkbox, clientId, containerClientId, checked, disabled, label, formattedValue);
        encodeOptionOutput(context, checkbox, checked);

        writer.endElement("div");
        writer.endElement("td");

        writer.startElement("td", null);
        encodeOptionLabel(context, checkbox, containerClientId, label);
        writer.endElement("td");
    }

}
