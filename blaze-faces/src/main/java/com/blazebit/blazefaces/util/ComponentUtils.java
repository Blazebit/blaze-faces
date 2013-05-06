/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.util;

import com.blazebit.blazefaces.apt.JsfFunction;
import com.blazebit.blazefaces.component.Widget;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UniqueIdVendor;
import javax.faces.component.ValueHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.validator.BeanValidator;

public class ComponentUtils {

    /**
     * Algorithm works as follows; - If it's an input component, submitted value
     * is checked first since it'd be the value to be used in case validation
     * errors terminates jsf lifecycle - Finally the value of the component is
     * retrieved from backing bean and if there's a converter, converted value
     * is returned
     *
     * @param context FacesContext instance
     * @param component UIComponent instance whose value will be returned
     * @return End text
     */
    public static String getValueToRender(FacesContext context, UIComponent component) {
        if (component instanceof ValueHolder) {

            if (component instanceof EditableValueHolder) {
                Object submittedValue = ((EditableValueHolder) component).getSubmittedValue();
                if (submittedValue != null) {
                    return submittedValue.toString();
                }
            }

            ValueHolder valueHolder = (ValueHolder) component;
            Object value = valueHolder.getValue();

            //format the value as string
            if (value != null) {
                Converter converter = getConverter(context, valueHolder);

                if (converter != null) {
                    return converter.getAsString(context, component, value);
                } else {
                    return value.toString();    //Use toString as a fallback if there is no explicit or implicit converter
                }
            } else {
                //component is a value holder but has no value
                return null;
            }
        }

        //component it not a value holder
        return null;
    }

    /**
     * Finds appropriate converter for a given value holder
     *
     * @param context FacesContext instance
     * @param component ValueHolder instance to look converter for
     * @return Converter
     */
    public static Converter getConverter(FacesContext context, ValueHolder component) {
        //explicit converter
        Converter converter = component.getConverter();

        //try to find implicit converter
        if (converter == null) {
            ValueExpression expr = ((UIComponent) component).getValueExpression("value");
            if (expr != null) {
                Class<?> valueType = expr.getType(context.getELContext());
                if (valueType != null) {
                    converter = context.getApplication().createConverter(valueType);
                }
            }
        }

        return converter;
    }

    /**
     * Algorithm works as follows; - If it's an input component, submitted value
     * is checked first since it'd be the value to be used in case validation
     * errors terminates jsf lifecycle - Finally the value of the component is
     * retrieved from backing bean and if there's a converter, converted value
     * is returned
     *
     * - If the component is not a value holder, toString of component is used
     * to support Facelets UIInstructions.
     *
     * @param context	FacesContext instance
     * @param component	UIComponent instance whose value will be returned
     * @return	End text
     */
    public static String getStringValueToRender(FacesContext facesContext, UIComponent component) {
        if (component instanceof ValueHolder) {

            if (component instanceof EditableValueHolder) {
                Object submittedValue = ((EditableValueHolder) component).getSubmittedValue();
                if (submittedValue != null) {
                    return submittedValue.toString();
                }
            }

            ValueHolder valueHolder = (ValueHolder) component;
            Object value = valueHolder.getValue();
            if (value == null) {
                return "";
            }

            //first ask the converter
            if (valueHolder.getConverter() != null) {
                return valueHolder.getConverter().getAsString(facesContext, component, value);
            } //Try to guess
            else {
                ValueExpression expr = component.getValueExpression("value");
                if (expr != null) {
                    Class<?> valueType = expr.getType(facesContext.getELContext());
                    if (valueType != null) {
                        Converter converterForType = facesContext.getApplication().createConverter(valueType);

                        if (converterForType != null) {
                            return converterForType.getAsString(facesContext, component, value);
                        }
                    }
                }
            }

            //No converter found just return the value as string
            return value.toString();
        } else {
            //This would get the plain texts on UIInstructions when using Facelets
            String value = component.toString();

            if (value != null) {
                return value.trim();
            } else {
                return "";
            }
        }
    }

    /**
     * Resolves the end text to render by using a specified value
     *
     * @param context	FacesContext instance
     * @param component	UIComponent instance whose value will be returned
     * @return	End text
     */
    public static String getStringValueToRender(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }

        ValueHolder valueHolder = (ValueHolder) component;

        Converter converter = valueHolder.getConverter();
        if (converter != null) {
            return converter.getAsString(facesContext, component, value);
        } else {
            ValueExpression expr = component.getValueExpression("value");
            if (expr != null) {
                Class<?> valueType = expr.getType(facesContext.getELContext());
                Converter converterForType = facesContext.getApplication().createConverter(valueType);

                if (converterForType != null) {
                    return converterForType.getAsString(facesContext, component, value);
                }
            }
        }

        return value.toString();
    }
    
	@JsfFunction
    public static UIComponent findParentForm(FacesContext context, UIComponent component) {
        UIComponent parent = component.getParent();

        while (parent != null) {
            if (parent instanceof UIForm) {
                return parent;
            }

            parent = parent.getParent();
        }

        return null;
    }

    public static UniqueIdVendor findParentUniqueIdVendor(UIComponent component) {
        UIComponent parent = component.getParent();

        while (parent != null) {
            if (parent instanceof UniqueIdVendor) {
                return (UniqueIdVendor) parent;
            }

            parent = parent.getParent();
        }

        return null;
    }

    public static UIComponent findParentNamingContainer(UIComponent component) {
        UIComponent parent = component.getParent();

        while (parent != null) {
            if (parent instanceof NamingContainer) {
                return (UIComponent) parent;
            }

            parent = parent.getParent();
        }

        return null;
    }

    public static void decorateAttribute(UIComponent component, String attribute, String value) {
        String attributeValue = (String) component.getAttributes().get(attribute);

        if (attributeValue != null) {
            if (attributeValue.indexOf(value) == -1) {
                String decoratedValue = attributeValue + ";" + value;

                component.getAttributes().put(attribute, decoratedValue);
            } else {
                component.getAttributes().put(attribute, attributeValue);
            }
        } else {
            component.getAttributes().put(attribute, value);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<SelectItem> createSelectItems(UIComponent component) {
        List<SelectItem> items = new ArrayList<SelectItem>();
        Iterator<UIComponent> children = component.getChildren().iterator();

        while (children.hasNext()) {
            UIComponent child = children.next();

            if (child instanceof UISelectItem) {
                UISelectItem selectItem = (UISelectItem) child;

                items.add(new SelectItem(selectItem.getItemValue(), selectItem.getItemLabel()));
            } else if (child instanceof UISelectItems) {
                Object selectItems = ((UISelectItems) child).getValue();

                if (selectItems instanceof SelectItem[]) {
                    SelectItem[] itemsArray = (SelectItem[]) selectItems;

                    for (SelectItem item : itemsArray) {
                        items.add(new SelectItem(item.getValue(), item.getLabel()));
                    }

                } else if (selectItems instanceof Collection) {
                    Collection<SelectItem> collection = (Collection<SelectItem>) selectItems;

                    for (SelectItem item : collection) {
                        items.add(new SelectItem(item.getValue(), item.getLabel()));
                    }
                }
            }
        }

        return items;
    }

    public static String escapeJQueryId(String id) {
        return "#" + id.replaceAll(":", "\\\\\\\\:");
    }

    public static String findClientIds(FacesContext facesContext, UIComponent component, String idList) {
        if (idList == null) {
            return "@none";
        }

        StringBuilder sb = new StringBuilder();
        String[] ids = idList.split("[,\\s]+");

        for (int i = 0; i < ids.length; i++) {
            if (i != 0) {
                sb.append(" ");
            }
            String id = ids[i].trim();

            if (id.equals("@all") || id.equals("@none")) {
                sb.append(id);
            } else if (id.equals("@this")) {
                sb.append(component.getClientId(facesContext));
            } else if (id.equals("@form")) {
                UIComponent form = ComponentUtils.findParentForm(facesContext, component);

                if (form == null) {
                    throw new FacesException("Component " + component.getClientId(facesContext) + " needs to be enclosed in a form");
                }

                sb.append(form.getClientId(facesContext));
            } else if (id.equals("@parent")) {
                sb.append(component.getParent().getClientId(facesContext));
            } else {
                UIComponent comp = component.findComponent(id);
                if (comp != null) {
                    sb.append(comp.getClientId(facesContext));
                } else {
                    throw new FacesException("Cannot find component with identifier \"" + id + "\" referenced from \"" + component.getClientId(facesContext) + "\".");
                }
            }
        }

        return sb.toString();
    }

    public static String findComponentClientId(String id) {
        UIComponent component = null;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        component = findComponent(facesContext.getViewRoot(), id);

        if (component == null) {
            throw new FacesException("Cannot find component " + id + " in view.");
        }

        return component.getClientId(facesContext);
    }

    public static UIComponent findComponent(UIComponent base, String id) {
        if (id.equals(base.getId())) {
            return base;
        }

        UIComponent kid = null;
        UIComponent result = null;
        Iterator<UIComponent> kids = base.getFacetsAndChildren();
        while (kids.hasNext() && (result == null)) {
            kid = (UIComponent) kids.next();
            if (id.equals(kid.getId())) {
                result = kid;
                break;
            }
            result = findComponent(kid, id);
            if (result != null) {
                break;
            }
        }
        return result;
    }
    public static String getWidgetVar(String id) {
	    UIComponent component = findComponent(FacesContext.getCurrentInstance().getViewRoot(), id);

        if(component == null) {
            throw new FacesException("Cannot find component " + id + " in view.");
        } else if(component instanceof Widget) {
            return ((Widget) component).resolveWidgetVar();
        } else {
            throw new FacesException("Component with id " + id + " is not a Widget");
        }
    }

    public static boolean isLiteralText(UIComponent component) {
        return component.getFamily().equalsIgnoreCase("facelets.LiteralText");
    }

    public static boolean componentIsDisabledOrReadonly(UIComponent component) {
        return Boolean.valueOf(String.valueOf(component.getAttributes().get("disabled"))) || Boolean.valueOf(String.valueOf(component.getAttributes().get("readonly")));
    }

    /**
     * Implementation from Apache Commons Lang
     */
    public static Locale toLocale(String str) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len != 2 && len != 5 && len < 7) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        char ch0 = str.charAt(0);
        char ch1 = str.charAt(1);
        if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len == 2) {
            return new Locale(str, "");
        } else {
            if (str.charAt(2) != '_') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            char ch3 = str.charAt(3);
            if (ch3 == '_') {
                return new Locale(str.substring(0, 2), "", str.substring(4));
            }
            char ch4 = str.charAt(4);
            if (ch3 < 'A' || ch3 > 'Z' || ch4 < 'A' || ch4 > 'Z') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            if (len == 5) {
                return new Locale(str.substring(0, 2), str.substring(3, 5));
            } else {
                if (str.charAt(5) != '_') {
                    throw new IllegalArgumentException("Invalid locale format: " + str);
                }
                return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
            }
        }
    }

    public static boolean validateEmptyFields(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        String value = externalContext.getInitParameter(UIInput.VALIDATE_EMPTY_FIELDS_PARAM_NAME);

        if (null == value) {
            value = (String) externalContext.getApplicationMap().get(UIInput.VALIDATE_EMPTY_FIELDS_PARAM_NAME);
        }

        if (value == null || value.equals("auto")) {
            return isBeansValidationAvailable(context);
        } else {
            return Boolean.valueOf(value);
        }
    }

    public static boolean isBeansValidationAvailable(FacesContext context) {
        boolean result = false;
        String beanValidationAvailableKey = "javax.faces.private.BEANS_VALIDATION_AVAILABLE";

        Map<String, Object> appMap = context.getExternalContext().getApplicationMap();

        if (appMap.containsKey(beanValidationAvailableKey)) {
            result = (Boolean) appMap.get(beanValidationAvailableKey);
        } else {
            try {
                new BeanValidator();
                appMap.put(beanValidationAvailableKey, result = true);
            } catch (Throwable t) {
                appMap.put(beanValidationAvailableKey, Boolean.FALSE);
            }
        }

        return result;
    }

    public static boolean isPartialSubmitEnabled(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        String value = externalContext.getInitParameter(Constants.SUBMIT_PARAM);

        return (value == null) ? false : value.equalsIgnoreCase("partial");
    }

    public static boolean isValueBlank(String value) {
        if (value == null) {
            return true;
        }

        return value.trim().equals("");
    }
}