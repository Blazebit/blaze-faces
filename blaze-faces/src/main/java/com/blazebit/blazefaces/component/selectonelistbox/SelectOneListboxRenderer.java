/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.selectonelistbox;

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

public class SelectOneListboxRenderer extends SelectRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        SelectOneListbox listbox = (SelectOneListbox) component;

        if(listbox.isDisabled()) {
            return;
        }

        decodeBehaviors(context, listbox);

        String clientId = listbox.getClientId(context);
        String value = context.getExternalContext().getRequestParameterMap().get(clientId + "_input");
        listbox.setSubmittedValue(value);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        SelectOneListbox listbox = (SelectOneListbox) component;

        encodeMarkup(context, listbox);
    }

    protected void encodeMarkup(FacesContext context, SelectOneListbox listbox) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = listbox.getClientId(context);
        Class<?> type = getValueType(context, listbox);
        
        String style = listbox.getStyle();
        String styleClass = listbox.getStyleClass();
//        styleClass = styleClass == null ? SelectOneListbox.CONTAINER_CLASS : SelectOneListbox.CONTAINER_CLASS + " " + styleClass;
        styleClass = listbox.isDisabled() ? styleClass + " ui-state-disabled" : styleClass;
        
        writer.startElement("div", listbox);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if(style != null) writer.writeAttribute("style", style, "style");

        encodeInput(context, listbox, clientId, type);
        encodeList(context, listbox);

        writer.endElement("div");
    }

    protected void encodeInput(FacesContext context, SelectOneListbox listbox, String clientId, Class<?> type) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String inputid = clientId + "_input";

        writer.startElement("div", listbox);
        writer.writeAttribute("class", "ui-helper-hidden", null);
        
        writer.startElement("select", listbox);
        writer.writeAttribute("id", inputid, "id");
        writer.writeAttribute("name", inputid, null);
        writer.writeAttribute("size", "2", null);   //prevent browser to send value when no item is selected
        if(listbox.getOnchange() != null) writer.writeAttribute("onchange", listbox.getOnchange(), null);

        encodeSelectItems(context, listbox, type);

        writer.endElement("select");

        writer.endElement("div");
    }

    protected void encodeList(FacesContext context, SelectOneListbox listbox) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("ul", null);

        writer.endElement("ul");
    }

    protected void encodeSelectItems(FacesContext context, SelectOneListbox listbox, Class<?> type) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        List<SelectItem> selectItems = getSelectItems(context, listbox);
        Converter converter = getConverter(context, listbox);
        Object value = listbox.getValue();

        for(SelectItem selectItem : selectItems) {
            Object itemValue = selectItem.getValue();
            String itemLabel = selectItem.getLabel();
            
            if(itemValue != null && !itemValue.equals("")) {
                itemValue = context.getApplication().getExpressionFactory().coerceToType(itemValue, type);
            }

            writer.startElement("option", null);
            writer.writeAttribute("value", getOptionAsString(context, listbox, converter, itemValue), null);

            if(value != null && value.equals(itemValue)) {
                writer.writeAttribute("selected", "selected", null);
            }

            writer.write(itemLabel);

            writer.endElement("option");
        }
    }
    
    protected Class<?> getValueType(FacesContext context, UIInput component) {
        ValueExpression ve = component.getValueExpression("value");
        Class<?> type = ve == null ? String.class : ve.getType(context.getELContext());
        
        return type == null ? String.class : type;
    }

}
