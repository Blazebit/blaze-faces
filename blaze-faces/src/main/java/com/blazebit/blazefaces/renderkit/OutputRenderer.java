/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.renderkit;


import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class OutputRenderer extends CoreRenderer {
    
    protected String getCurrentValue(FacesContext context, UIComponent component) {
        String currentValue = null;
        Object currentObj = ((ValueHolder) component).getValue();

        if (currentObj != null) {
            currentValue = getFormattedValue(context, component, currentObj, ((ValueHolder) component).getConverter());
        }

        return currentValue;
    }
    
    protected Converter getConverter(FacesContext context, UIComponent component){
        ValueHolder valHolder = (ValueHolder) component;
        Converter converter = valHolder.getConverter();
        
        if (converter == null) {
            Object value = valHolder.getValue();
            
            if(value != null){
                Class<?> converterType = value.getClass();
                converter = context.getApplication().createConverter(converterType);
            }
        }
        
        return converter;
    }

    protected String getFormattedValue(FacesContext context, UIComponent component, Object currentValue, Converter converter) throws ConverterException {
        if (!(component instanceof ValueHolder)) {
            if (currentValue != null) {
                return currentValue.toString();
            }
            return null;
        }

        if (converter == null) {
            converter = ((ValueHolder) component).getConverter();
        }

        if (converter == null) {
            if (currentValue == null) {
                return "";
            }

            if (currentValue instanceof String) {
                return (String) currentValue;
            }

            Class<?> converterType = currentValue.getClass();
            converter = context.getApplication().createConverter(converterType);

            if (converter == null) {
                return currentValue.toString();
            }
        }

        return converter.getAsString(context, component, currentValue);
    }
}
