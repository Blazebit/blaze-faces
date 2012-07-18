/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.selectoneradio;


import java.io.IOException;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import com.blazebit.blazefaces.renderkit.SelectRenderer;

public class SelectOneRadioRenderer extends SelectRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        SelectOneRadio radio = (SelectOneRadio) component;

        if(radio.isDisabled()) {
            return;
        }

        decodeBehaviors(context, radio);

        String clientId = radio.getClientId(context);
        String value = context.getExternalContext().getRequestParameterMap().get(clientId);
        
        radio.setSubmittedValue(value);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        SelectOneRadio radio = (SelectOneRadio) component;

        encodeMarkup(context, radio);
    }

    protected void encodeMarkup(FacesContext context, SelectOneRadio radio) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = radio.getClientId(context);
        String style = radio.getStyle();
        String styleClass = radio.getStyleClass();
//        styleClass = styleClass == null ? SelectOneRadio.STYLE_CLASS : SelectOneRadio.STYLE_CLASS + " " + styleClass;

        writer.startElement("table", radio);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if(style != null)
            writer.writeAttribute("style", style, "style");

        encodeSelectItems(context, radio);

        writer.endElement("table");
    }

    protected void encodeOptionInput(FacesContext context, SelectOneRadio radio, String clientId, String containerClientId, boolean checked, boolean disabled, String label, String value) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute("class", "ui-radiobutton-inputwrapper", null);

        writer.startElement("input", null);
        writer.writeAttribute("id", containerClientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("type", "radio", null);
        writer.writeAttribute("value", value, null);

        if(checked) writer.writeAttribute("checked", "checked", null);
        if(disabled) writer.writeAttribute("disabled", "disabled", null);
        if(radio.getOnchange() != null) writer.writeAttribute("onchange", radio.getOnchange(), null);

        writer.endElement("input");

        writer.endElement("div");
    }

    protected void encodeOptionLabel(FacesContext context, SelectOneRadio radio, String containerClientId, String label) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("label", null);
        writer.writeAttribute("for", containerClientId, null);
        writer.write(label);
        writer.endElement("label");
    }

    protected void encodeOptionOutput(FacesContext context, SelectOneRadio radio, boolean checked) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String styleClass = "ui-radiobutton-box ui-widget ui-corner-all ui-radiobutton-relative ui-state-default";
        styleClass = checked ? styleClass + " ui-state-active" : styleClass;

        String iconClass = "ui-radiobutton-icon";
        iconClass = checked ? iconClass + " ui-icon ui-icon-bullet" : iconClass;

        writer.startElement("div", null);
        writer.writeAttribute("class", styleClass, null);

        writer.startElement("span", null);
        writer.writeAttribute("class", iconClass, null);
        writer.endElement("span");

        writer.endElement("div");
    }

    protected void encodeSelectItems(FacesContext context, SelectOneRadio radio) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        List<SelectItem> selectItems = getSelectItems(context, radio);
        String layout = radio.getLayout();
        boolean pageDirection = layout != null && layout.equals("pageDirection");

        for(SelectItem selectItem : selectItems) {
            Object itemValue = selectItem.getValue();
            String itemLabel = selectItem.getLabel();

            if(pageDirection) {
                writer.startElement("tr", null);
            }

            encodeOption(context, radio, itemLabel, itemValue);

            if(pageDirection) {
                writer.endElement("tr");
            }
        }
    }

    protected void encodeOption(FacesContext context, SelectOneRadio radio, String itemLabel, Object itemValue) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Object value = radio.getValue();
        Converter converter = getConverter(context, radio);
        
        String convertedValue = getOptionAsString(context, radio, converter, itemValue);
        String clientId = radio.getClientId(context);
        String containerClientId = radio.getContainerClientId(context);
        boolean disabled = radio.isDisabled();
        Class<?> type = getValueType(context, radio);
        
        if(itemValue != null && !itemValue.equals("")) {
            itemValue = context.getApplication().getExpressionFactory().coerceToType(itemValue, type);
        }
        
        boolean checked = value != null && value.equals(itemValue);

        writer.startElement("td", null);

        String styleClass = "ui-radiobutton ui-widget";
        if(disabled) {
            styleClass += " ui-state-disabled";
        }

        writer.startElement("div", null);
        writer.writeAttribute("class", styleClass, null);

        encodeOptionInput(context, radio, clientId, containerClientId, checked, disabled, itemLabel, convertedValue);
        encodeOptionOutput(context, radio, checked);

        writer.endElement("div");
        writer.endElement("td");

        writer.startElement("td", null);
        encodeOptionLabel(context, radio, containerClientId, itemLabel);
        writer.endElement("td");
    }
    
    protected Class<?> getValueType(FacesContext context, UIInput input) {
        ValueExpression ve = input.getValueExpression("value");
        Class<?> type = ve == null ? String.class : ve.getType(context.getELContext());
        
        return type == null ? String.class : type;
    }

}
