/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.selectmanymenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;

import com.blazebit.blazefaces.renderkit.SelectRenderer;
import com.blazebit.blazefaces.util.ComponentUtil;

public class SelectManyMenuRenderer extends SelectRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        SelectManyMenu comp = (SelectManyMenu) component;

        if (ComponentUtil.componentIsDisabledOrReadonly(component)) {
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
        ResponseWriter writer = context.getResponseWriter();
        SelectManyMenu comp = (SelectManyMenu) component;
        String clientId = comp.getClientId(context);
        
        writer.startElement("select", comp);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("multiple", "multiple", null);
        writer.writeAttribute("size", "2", null);   //prevent browser to send value when no item is selected
//        if(menu.getOnchange() != null) writer.writeAttribute("onchange", menu.getOnchange(), null);

        encodeSelectItems(context, comp);
        writer.endElement("select");
        encodeBehaviors(context, (ClientBehaviorHolder) component);
    }
    
    protected void encodeList(FacesContext context, SelectManyMenu menu) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("ul", null);

        writer.endElement("ul");
    }

    @SuppressWarnings("unchecked")
	@Override
	public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
		SelectManyMenu menu = (SelectManyMenu) component;
		String[] values = (String[]) submittedValue;
		Converter converter = getConverter(context, menu);
        List<Object> list = null;

        if(converter != null) {
            list = new ArrayList<Object>();

            for(String value : values) {
                list.add(converter.getAsObject(context, menu, value));
            }
        }
        else {
            list = (List<Object>) (List<?>) Arrays.asList(values);
        }

        return list;
	}

    @SuppressWarnings("unchecked")
	protected void encodeSelectItems(FacesContext context, SelectManyMenu menu) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        List<SelectItem> selectItems = getSelectItems(context, menu);
        Converter converter = getConverter(context, menu);
        List<Object> value = (List<Object>) menu.getValue();

        for(SelectItem selectItem : selectItems) {
            Object itemValue = selectItem.getValue();
            String itemLabel = selectItem.getLabel();

            writer.startElement("option", null);
            writer.writeAttribute("value", getOptionAsString(context, menu, converter, itemValue), null);

            if(value != null && value.contains(itemValue)) {
                writer.writeAttribute("selected", "selected", null);
            }

            writer.write(itemLabel);

            writer.endElement("option");
        }
    }

}
