/*
 * Copyright 2011-2012 Blazebit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blazebit.blazefaces.component.selectmanycheckbox;

import java.io.IOException;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import com.blazebit.blazefaces.renderkit.SelectManyRenderer;
import com.blazebit.blazefaces.util.HTML;

public class SelectManyCheckboxRenderer extends SelectManyRenderer {

    @Override
	public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        return context.getRenderKit().getRenderer("javax.faces.SelectMany", "javax.faces.Checkbox").getConvertedValue(context, component, submittedValue);
	}

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        SelectManyCheckbox checkbox = (SelectManyCheckbox) component;

        encodeMarkup(context, checkbox);
        encodeScript(context, checkbox);
    }

    protected void encodeMarkup(FacesContext context, SelectManyCheckbox checkbox) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = checkbox.getClientId(context);
        String style = checkbox.getStyle();
        String styleClass = checkbox.getStyleClass();
        styleClass = styleClass == null ? SelectManyCheckbox.STYLE_CLASS : SelectManyCheckbox.STYLE_CLASS + " " + styleClass;
        
        writer.startElement("table", checkbox);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if(style != null)
            writer.writeAttribute("style", style, "style");

        encodeSelectItems(context, checkbox);

        writer.endElement("table");
    }

    protected void encodeScript(FacesContext context, SelectManyCheckbox checkbox) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = checkbox.getClientId(context);

        startScript(writer, clientId);
        
        writer.write("BlazeFaces.cw('SelectManyCheckbox','" + checkbox.resolveWidgetVar() + "',{");
        writer.write("id:'" + clientId + "'");

        encodeClientBehaviors(context, checkbox);

        writer.write("});");

        endScript(writer);
    }

    protected void encodeOptionInput(FacesContext context, SelectManyCheckbox checkbox, String id, String name, boolean checked, boolean disabled, String value) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        writer.startElement("div", null);
        writer.writeAttribute("class", "ui-helper-hidden-accessible", null);

        writer.startElement("input", null);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("name", name, null);
        writer.writeAttribute("type", "checkbox", null);
        writer.writeAttribute("value", value, null);

        if(checked) writer.writeAttribute("checked", "checked", null);
        if(disabled) writer.writeAttribute("disabled", "disabled", null);
        if(checkbox.getOnchange() != null) writer.writeAttribute("onchange", checkbox.getOnchange(), null);

        writer.endElement("input");

        writer.endElement("div");
    }

    protected void encodeOptionLabel(FacesContext context, SelectManyCheckbox checkbox, String containerClientId, SelectItem option, boolean disabled) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        writer.startElement("label", null);
        writer.writeAttribute("for", containerClientId, null);
        if(disabled)
            writer.writeAttribute("class", "ui-state-disabled", null);
        
        if(option.isEscape())
            writer.writeText(option.getLabel(),null);
        else
            writer.write(option.getLabel());
        
        writer.endElement("label");
    }

    protected void encodeOptionOutput(FacesContext context, SelectManyCheckbox checkbox, boolean checked, boolean disabled) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String boxClass = HTML.CHECKBOX_BOX_CLASS;
        boxClass = checked ? boxClass + " ui-state-active" : boxClass;
        boxClass = disabled ? boxClass + " ui-state-disabled" : boxClass;
        boxClass = !checkbox.isValid() ? boxClass + " ui-state-error" : boxClass;
        
        String iconClass = HTML.CHECKBOX_ICON_CLASS;
        iconClass = checked ? iconClass + " " + HTML.CHECKBOX_CHECKED_ICON_CLASS : iconClass;
        
        writer.startElement("div", null);
        writer.writeAttribute("class", boxClass, null);

        writer.startElement("span", null);
        writer.writeAttribute("class", iconClass, null);
        writer.endElement("span");

        writer.endElement("div");
    }

    protected void encodeSelectItems(FacesContext context, SelectManyCheckbox checkbox) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        List<SelectItem> selectItems = getSelectItems(context, checkbox);
        Converter converter = checkbox.getConverter();
        Object values = getValues(checkbox);
        Object submittedValues = getSubmittedValues(checkbox);
        String layout = checkbox.getLayout();
        boolean pageDirection = layout != null && layout.equals("pageDirection");

        int idx = -1;
        for(SelectItem selectItem : selectItems) {
            idx++;
            if(pageDirection) {
                writer.startElement("tr", null);
            }

            encodeOption(context, checkbox, values, submittedValues, converter, selectItem, idx);

            if(pageDirection) {
                writer.endElement("tr");
            }
        }
    }

    protected void encodeOption(FacesContext context, UIInput component, Object values, Object submittedValues, Converter converter, SelectItem option, int idx) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        SelectManyCheckbox checkbox = (SelectManyCheckbox) component;
        String itemValueAsString = getOptionAsString(context, component, converter, option.getValue());
        String name = checkbox.getClientId(context);
        String id = name + UINamingContainer.getSeparatorChar(context) + idx;
        boolean disabled = option.isDisabled() || checkbox.isDisabled();

        Object valuesArray;
        Object itemValue;
        if(submittedValues != null) {
            valuesArray = submittedValues;
            itemValue = itemValueAsString;
        } else {
            valuesArray = values;
            itemValue = option.getValue();
        }
        
        boolean selected = isSelected(context, component, itemValue, valuesArray, converter);
        if(option.isNoSelectionOption() && values != null && !selected) {
            return;
        }
        
        writer.startElement("td", null);
        
        writer.startElement("div", null);
        writer.writeAttribute("class", HTML.CHECKBOX_CLASS, null);

        encodeOptionInput(context, checkbox, id, name, selected, disabled, itemValueAsString);
        encodeOptionOutput(context, checkbox, selected, disabled);

        writer.endElement("div");
        writer.endElement("td");

        writer.startElement("td", null);
        encodeOptionLabel(context, checkbox, id, option, disabled);
        writer.endElement("td");
    }
    
    @Override
    protected String getSubmitParam(FacesContext context, UISelectMany selectMany) {
        return selectMany.getClientId(context);
    }
    
    // Old implementation
    // TODO: merge code
//    @Override
//    public void decode(FacesContext context, UIComponent component) {
//        SelectManyCheckbox comp = (SelectManyCheckbox) component;
//
//        if (ComponentUtils.componentIsDisabledOrReadonly(component)) {
//            return;
//        }
//
//        decodeBehaviors(context, comp);
//
//        String clientId = comp.getClientId(context);
//        String[] submittedValues = context.getExternalContext().getRequestParameterValuesMap().get(clientId);
//
//        if (submittedValues != null) {
//            comp.setSubmittedValue(submittedValues);
//        } else {
//            comp.setSubmittedValue(new String[0]);
//        }
//    }
//
//    @Override
//    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
//        SelectManyCheckbox comp = (SelectManyCheckbox) component;
//        
//        encodeSelectItems(context, comp);
//        encodeBehaviors(context, (ClientBehaviorHolder) component);
//    }
//    
//        protected void encodeOptionInput(FacesContext context, SelectManyCheckbox checkbox, String clientId, String containerClientId, boolean checked, boolean disabled, String label, String formattedValue) throws IOException {
//        ResponseWriter writer = context.getResponseWriter();
//        
//        writer.startElement("div", null);
//        writer.writeAttribute("class", "ui-checkbox-inputwrapper", null);
//
//        writer.startElement("input", null);
//        writer.writeAttribute("id", containerClientId, null);
//        writer.writeAttribute("name", clientId, null);
//        writer.writeAttribute("type", "checkbox", null);
//        writer.writeAttribute("value", formattedValue, null);
//
//        if(checked) writer.writeAttribute("checked", "checked", null);
//        if(disabled) writer.writeAttribute("disabled", "disabled", null);
//        if(checkbox.getOnchange() != null) writer.writeAttribute("onchange", checkbox.getOnchange(), null);
//        renderPassThruAttributes(context, checkbox, HTML5.COMMON_ATTRIBUTES);
//
//        writer.endElement("input");
//
//        writer.endElement("div");
//    }
//
//    protected void encodeOptionLabel(FacesContext context, SelectManyCheckbox checkbox, String containerClientId, String label) throws IOException {
//        ResponseWriter writer = context.getResponseWriter();
//
//        writer.startElement("label", null);
//        writer.writeAttribute("for", containerClientId, null);
//        writer.write(label);
//        writer.endElement("label");
//    }
//
//    protected void encodeOptionOutput(FacesContext context, SelectManyCheckbox checkbox, boolean checked) throws IOException {
//        ResponseWriter writer = context.getResponseWriter();
//        String styleClass = "ui-checkbox-box ui-widget ui-corner-all ui-checkbox-relative ui-state-default";
//        styleClass = checked ? styleClass + " ui-state-active" : styleClass;
//
//        String iconClass = "ui-checkbox-icon";
//        iconClass = checked ? iconClass + " ui-icon ui-icon-check" : iconClass;
//
//        writer.startElement("div", null);
//        writer.writeAttribute("class", styleClass, null);
//
//        writer.startElement("span", null);
//        writer.writeAttribute("class", iconClass, null);
//        writer.endElement("span");
//
//        writer.endElement("div");
//    }
//
//    protected void encodeSelectItems(FacesContext context, SelectManyCheckbox checkbox) throws IOException {
//        ResponseWriter writer = context.getResponseWriter();
//        List<SelectItem> selectItems = getSelectItems(context, checkbox);
//        Converter converter = getConverter(context, checkbox);
//        Object value = checkbox.getValue();
//        String layout = checkbox.getLayout();
//        boolean pageDirection = layout != null && layout.equals("pageDirection");
//
//        for(SelectItem selectItem : selectItems) {
//            Object itemValue = selectItem.getValue();
//            String itemLabel = selectItem.getLabel();
//
//            if(pageDirection) {
//                writer.startElement("tr", null);
//            }
//
//            encodeOption(context, checkbox, value, converter, itemLabel, itemValue);
//
//            if(pageDirection) {
//                writer.endElement("tr");
//            }
//        }
//    }
//
//    protected void encodeOption(FacesContext context, UIInput component, Object componentValue, Converter converter, String label, Object value) throws IOException {
//        ResponseWriter writer = context.getResponseWriter();
//        SelectManyCheckbox checkbox = (SelectManyCheckbox) component;
//        String formattedValue = getOptionAsString(context, component, converter, value);
//        String clientId = component.getClientId(context);
//        String containerClientId = component.getContainerClientId(context);
//        boolean checked = componentValue != null && ((List<?>) componentValue).contains(value);
//        boolean disabled = checkbox.isDisabled();
//
//        writer.startElement("td", null);
//
//        String styleClass = "ui-checkbox ui-widget";
//        if(disabled) {
//            styleClass += " ui-state-disabled";
//        }
//
//        writer.startElement("div", null);
//        writer.writeAttribute("class", styleClass, null);
//
//        encodeOptionInput(context, checkbox, clientId, containerClientId, checked, disabled, label, formattedValue);
//        encodeOptionOutput(context, checkbox, checked);
//
//        writer.endElement("div");
//        writer.endElement("td");
//
//        writer.startElement("td", null);
//        encodeOptionLabel(context, checkbox, containerClientId, label);
//        writer.endElement("td");
//    }
}
