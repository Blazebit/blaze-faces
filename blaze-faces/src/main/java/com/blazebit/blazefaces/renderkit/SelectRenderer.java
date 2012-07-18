/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.renderkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;

/**
 *
 * @author Christian Beikov
 */
public class SelectRenderer extends InputRenderer {

    protected List<SelectItem> getSelectItems(FacesContext context, UIInput component) {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();

        for (UIComponent child : component.getChildren()) {
            if (child instanceof UISelectItem) {
                UISelectItem uiSelectItem = (UISelectItem) child;

                selectItems.add(new SelectItem(uiSelectItem.getItemValue(), uiSelectItem.getItemLabel()));
            } else if (child instanceof UISelectItems) {
                UISelectItems uiSelectItems = ((UISelectItems) child);
                Object value = uiSelectItems.getValue();

                if (value instanceof SelectItem[]) {
                    selectItems.addAll(Arrays.asList((SelectItem[]) value));
                } else if (value instanceof Map) {
                    Map map = (Map) value;

                    for (Iterator it = map.keySet().iterator(); it.hasNext();) {
                        Object key = it.next();

                        selectItems.add(new SelectItem(map.get(key), String.valueOf(key)));
                    }
                } else if (value instanceof Collection) {
                    Collection collection = (Collection) value;
                    String var = (String) uiSelectItems.getAttributes().get("var");

                    if (var != null) {
                        for (Iterator it = collection.iterator(); it.hasNext();) {
                            Object object = it.next();
                            context.getExternalContext().getRequestMap().put(var, object);
                            String itemLabel = (String) uiSelectItems.getAttributes().get("itemLabel");
                            Object itemValue = uiSelectItems.getAttributes().get("itemValue");

                            selectItems.add(new SelectItem(itemValue, itemLabel));
                        }
                    } else {
                        for (Iterator it = collection.iterator(); it.hasNext();) {
                            Object object = it.next();

                            if (object instanceof SelectItem) {
                                selectItems.add((SelectItem) object);
                            } else if (object instanceof Enum) {
                                Enum e = (Enum) object;
                                selectItems.add(new SelectItem(e.name(), e.name()));
                            }
                        }
                    }
                }
            }
        }

        return selectItems;
    }

    protected String getOptionAsString(FacesContext context, UIInput component, Converter converter, Object value) {
        if (converter != null) {
            return converter.getAsString(context, component, value);
        } else if (value == null) {
            return "";
        } else {
            return value.toString();
        }
    }

    protected Converter getConverter(FacesContext context, UIInput component) {
        Converter converter = component.getConverter();

        if (converter != null) {
            return converter;
        } else {
            ValueExpression ve = component.getValueExpression("value");

            if (ve != null) {
                Class<?> valueType = ve.getType(context.getELContext());

                if (valueType != null) {
                    return context.getApplication().createConverter(valueType);
                }
            }
        }

        return null;
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        UIInput input = (UIInput) component;
        Converter converter = getConverter(context, input);

        if (converter != null) {
            return converter.getAsObject(context, component, (String) submittedValue);
        } else {
            return submittedValue;
        }
    }
}
