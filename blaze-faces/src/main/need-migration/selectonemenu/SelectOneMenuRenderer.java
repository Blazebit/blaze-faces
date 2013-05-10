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
package com.blazebit.blazefaces.component.selectonemenu;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import com.blazebit.blazefaces.component.column.Column;
import com.blazebit.blazefaces.renderkit.SelectOneRenderer;
import com.blazebit.blazefaces.util.ComponentUtils;

public class SelectOneMenuRenderer extends SelectOneRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        if(!shouldDecode(component)) {
            return;
        }
        
        SelectOneMenu menu = (SelectOneMenu) component;
        if(menu.isEditable()) {
            Map<String,String> params = context.getExternalContext().getRequestParameterMap();
            
            decodeBehaviors(context, menu);
            
            menu.setSubmittedValue(params.get(menu.getClientId(context) + "_editableInput"));
        }
        else {
            super.decode(context, component);
        }
    }
    
    @Override
	public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        return context.getRenderKit().getRenderer("javax.faces.SelectOne", "javax.faces.Menu").getConvertedValue(context, component, submittedValue);
	}
    
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        SelectOneMenu menu = (SelectOneMenu) component;

        encodeMarkup(context, menu);
        encodeScript(context, menu);
    }

    protected void encodeMarkup(FacesContext context, SelectOneMenu menu) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        List<SelectItem> selectItems = getSelectItems(context, menu);
        String clientId = menu.getClientId(context);
        Converter converter = menu.getConverter();
        Object values = getValues(menu);
        Object submittedValues = getSubmittedValues(menu);
        boolean valid = menu.isValid();
                
        String style = menu.getStyle();
        String styleClass = menu.getStyleClass();
        styleClass = styleClass == null ? SelectOneMenu.STYLE_CLASS : SelectOneMenu.STYLE_CLASS + " " + styleClass;
        styleClass = !valid ? styleClass + " ui-state-error" : styleClass;
        styleClass = menu.isDisabled() ? styleClass + " ui-state-disabled" : styleClass;

        writer.startElement("div", menu);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleclass");
        if(style != null) {
            writer.writeAttribute("style", style, "style");
        }

        encodeInput(context, menu, clientId, selectItems, values, submittedValues, converter);
        encodeLabel(context, menu, selectItems);
        encodeMenuIcon(context, menu, valid);
        encodePanel(context, menu, selectItems);

        writer.endElement("div");
    }

    protected void encodeInput(FacesContext context, SelectOneMenu menu, String clientId, List<SelectItem> selectItems, Object values, Object submittedValues, Converter converter) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String inputId = clientId + "_input";
        
        writer.startElement("div", menu);
        writer.writeAttribute("class", "ui-helper-hidden-accessible", null);

        writer.startElement("select", menu);
        writer.writeAttribute("id", inputId, "id");
        writer.writeAttribute("name", inputId, null);
        if(menu.isDisabled()) writer.writeAttribute("disabled", "disabled", null);
        if(menu.getTabindex() != null) writer.writeAttribute("tabindex", menu.getTabindex(), null);
        if(menu.getOnkeydown() != null) writer.writeAttribute("onkeydown", menu.getOnkeydown(), null);
        if(menu.getOnkeyup() != null) writer.writeAttribute("onkeyup", menu.getOnkeyup(), null);
        
        encodeSelectItems(context, menu, selectItems, values, submittedValues, converter);

        writer.endElement("select");

        writer.endElement("div");
    }

    protected void encodeLabel(FacesContext context, SelectOneMenu menu, List<SelectItem> selectItems) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String valueToRender = ComponentUtils.getValueToRender(context, menu);
        
        if(menu.isEditable()) {
            writer.startElement("input", null);
            writer.writeAttribute("type", "text", null);
            writer.writeAttribute("name", menu.getClientId() + "_editableInput", null);
            writer.writeAttribute("class", SelectOneMenu.LABEL_CLASS, null);
            writer.writeAttribute("tabindex", -1, null);
            writer.writeAttribute("autocomplete", "off", null);
            if(menu.isDisabled()) {
                writer.writeAttribute("disabled", "disabled", null);
            }

            if(valueToRender != null) {
                writer.writeAttribute("value", valueToRender , null);
            }

            writer.endElement("input");
        }
        else {
            writer.startElement("label", null);
            writer.writeAttribute("id", menu.getClientId() + "_label", null);
            writer.writeAttribute("class", SelectOneMenu.LABEL_CLASS, null);
            writer.write("&nbsp;");
            writer.endElement("label");
        }
    }

    protected void encodeMenuIcon(FacesContext context, SelectOneMenu menu, boolean valid) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String iconClass = valid ? SelectOneMenu.TRIGGER_CLASS : SelectOneMenu.TRIGGER_CLASS + " ui-state-error";
        
        writer.startElement("div", menu);
        writer.writeAttribute("class", iconClass, null);

        writer.startElement("span", menu);
        writer.writeAttribute("class", "ui-icon ui-icon-triangle-1-s", null);
        writer.endElement("span");

        writer.endElement("div");
    }

    protected void encodePanel(FacesContext context, SelectOneMenu menu, List<SelectItem> selectItems) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        boolean customContent = menu.getVar() != null;
        int height = calculatePanelHeight(menu, selectItems.size());
        String panelStyle = menu.getPanelStyle() != null ? menu.getPanelStyle() : "";
        String panelStyleClass = menu.getPanelStyleClass();
        panelStyleClass = panelStyleClass == null ? SelectOneMenu.PANEL_CLASS : SelectOneMenu.PANEL_CLASS + " " + panelStyleClass;
             
        if(height != -1) {
            panelStyle += ";height:" + height + "px";
        }
        
        writer.startElement("div", null);
        writer.writeAttribute("id", menu.getClientId(context) + "_panel", null);
        writer.writeAttribute("class", panelStyleClass, null);
        
        if(!isValueEmpty(panelStyle)) {
            writer.writeAttribute("style", panelStyle, null);
        }

        if(customContent) {
            writer.startElement("table", menu);
            writer.writeAttribute("class", SelectOneMenu.TABLE_CLASS, null);
            writer.startElement("tbody", menu);
            encodeOptionsAsTable(context, menu, selectItems);
            writer.endElement("tbody");
            writer.endElement("table");
        } 
        else {
            writer.startElement("ul", menu);
            writer.writeAttribute("class", SelectOneMenu.LIST_CLASS, null);
            encodeOptionsAsList(context, menu, selectItems);
            writer.endElement("ul");
        }
        
        writer.endElement("div");
    }

    protected void encodeOptionsAsTable(FacesContext context, SelectOneMenu menu, List<SelectItem> selectItems) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String var = menu.getVar();
        List<Column> columns = menu.getColums();
        Object value = menu.getValue();

        for(SelectItem selectItem : selectItems) {
            Object itemValue = selectItem.getValue();
            
            context.getExternalContext().getRequestMap().put(var, selectItem.getValue());
            
            writer.startElement("tr", null);
            writer.writeAttribute("class", SelectOneMenu.ROW_CLASS, null);
            if(selectItem.getDescription() != null) {
                writer.writeAttribute("title", selectItem.getDescription(), null);
            }

            if(itemValue instanceof String) {
                writer.startElement("td", null);
                writer.writeAttribute("colspan", columns.size(), null);
                writer.writeText(selectItem.getLabel(), null);
                writer.endElement("td");
            } 
            else {
                for(Column column : columns) {
                    writer.startElement("td", null);
                    column.encodeAll(context);
                    writer.endElement("td");
                }
            }

            writer.endElement("tr");
        }

        context.getExternalContext().getRequestMap().put(var, null);
    }

    protected void encodeOptionsAsList(FacesContext context, SelectOneMenu menu, List<SelectItem> selectItems) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Object value = menu.getValue();

        for(int i = 0; i < selectItems.size(); i++) {
            SelectItem selectItem = selectItems.get(i);
            String itemLabel = selectItem.getLabel();
            itemLabel = isValueBlank(itemLabel) ? "&nbsp;" : itemLabel;
            
            writer.startElement("li", null);
            writer.writeAttribute("class", SelectOneMenu.ITEM_CLASS, null);
            if(selectItem.getDescription() != null) {
                writer.writeAttribute("title", selectItem.getDescription(), null);
            }
            
            if(itemLabel.equals("&nbsp;"))
                writer.write(itemLabel);
            else {
                if(selectItem.isEscape())
                    writer.writeText(itemLabel, "value");
                else
                    writer.write(itemLabel);
            }
                

            writer.endElement("li");
        }
    }

    protected void encodeScript(FacesContext context, SelectOneMenu menu) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = menu.getClientId(context);

        startScript(writer, clientId);
        
        writer.write("$(function(){");
        writer.write("BlazeFaces.cw('SelectOneMenu','" + menu.resolveWidgetVar() + "',{");
        writer.write("id:'" + clientId + "'");
        
        if(menu.getEffect() != null) writer.write(",effect:'" + menu.getEffect() + "'");
        if(menu.getEffectSpeed() != null) writer.write(",effectSpeed:'" + menu.getEffectSpeed() + "'");
        if(menu.isEditable())  writer.write(",editable:true");
        if(menu.getOnchange() != null) writer.write(",onchange:function() {" + menu.getOnchange() + "}");

        encodeClientBehaviors(context, menu);

        writer.write("});});");

        endScript(writer);
    }

    protected void encodeSelectItems(FacesContext context, SelectOneMenu menu, List<SelectItem> selectItems, Object values, Object submittedValues, Converter converter) throws IOException {
        for(SelectItem selectItem : selectItems) {
            encodeOption(context, menu, selectItem, values, submittedValues, converter);
        }
    }
    
    protected void encodeOption(FacesContext context, SelectOneMenu menu, SelectItem option, Object values, Object submittedValues, Converter converter) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String itemValueAsString = getOptionAsString(context, menu, converter, option.getValue());
        boolean disabled = option.isDisabled();

        Object valuesArray;
        Object itemValue;
        if(submittedValues != null) {
            valuesArray = submittedValues;
            itemValue = itemValueAsString;
        } else {
            valuesArray = values;
            itemValue = option.getValue();
        }

        boolean selected = isSelected(context, menu, itemValue, valuesArray, converter);
        if(option.isNoSelectionOption() && values != null && !selected) {
            return;
        }

        writer.startElement("option", null);
        writer.writeAttribute("value", itemValueAsString, null);
        if(disabled) writer.writeAttribute("disabled", "disabled", null);
        if(selected) writer.writeAttribute("selected", "selected", null);

        if(option.isEscape())
            writer.writeText(option.getLabel(), "value");
        else
            writer.write(option.getLabel());

        writer.endElement("option");
    }

    protected int calculatePanelHeight(SelectOneMenu menu, int itemSize) {
        int height = menu.getHeight();
        
        if(height != Integer.MAX_VALUE) {
            return height;
        } else if(itemSize > 10) {
            return 200;
        }
        
        return -1;
    }

    @Override
    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
		//Rendering happens on encodeEnd
	}

    @Override
	public boolean getRendersChildren() {
		return true;
	}

    @Override
    protected String getSubmitParam(FacesContext context, UISelectOne selectOne) {
        return selectOne.getClientId(context) + "_input";
    }
    
    // Old implementation
    // TODO: merge code
//    @Override
//    public void decode(FacesContext context, UIComponent component) {
//        SelectOneMenu menu = (SelectOneMenu) component;
//
//        if(menu.isDisabled() || menu.isReadonly()) {
//            return;
//        }
//
//        decodeBehaviors(context, menu);
//
//        String clientId = menu.getClientId(context);
//        String value = context.getExternalContext().getRequestParameterMap().get(clientId + "_input");
//
//        if(value != null) {
//            menu.setSubmittedValue(value);
//        }
//    }
//
//    @Override
//    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
//        SelectOneMenu menu = (SelectOneMenu) component;
//
//        encodeMarkup(context, menu);
//    }
//
//    protected void encodeMarkup(FacesContext context, SelectOneMenu menu) throws IOException {
//        ResponseWriter writer = context.getResponseWriter();
//        List<SelectItem> selectItems = getSelectItems(context, menu);
//        String clientId = menu.getClientId(context);
//        boolean disabled = menu.isDisabled();
//        Class<?> type = getValueType(context, menu);
//                
//        String style = menu.getStyle();
//        String styleclass = menu.getStyleClass();
//// styleclass = styleclass == null ? SelectOneMenu.STYLE_CLASS : SelectOneMenu.STYLE_CLASS + " " + styleclass;
//        styleclass = disabled ? styleclass + " ui-state-disabled" : styleclass;
//
//        writer.startElement("div", menu);
//        writer.writeAttribute("id", clientId, "id");
//        writer.writeAttribute("class", styleclass, "styleclass");
//        if(style != null)
//            writer.writeAttribute("style", style, "style");
//
//        encodeInput(context, menu, clientId, selectItems, type);
//        encodeLabel(context, menu, selectItems, type);
//        encodeMenuIcon(context, menu);
//        encodePanel(context, menu, selectItems, type);
//
//        writer.endElement("div");
//    }
//
//    protected void encodeInput(FacesContext context, SelectOneMenu menu, String clientId, List<SelectItem> selectItems, Class<?> type) throws IOException {
//        ResponseWriter writer = context.getResponseWriter();
//        String inputId = clientId + "_input";
//        
//        writer.startElement("div", menu);
//        writer.writeAttribute("class", "ui-helper-hidden", null);
//
//        writer.startElement("select", menu);
//        writer.writeAttribute("id", inputId, "id");
//        writer.writeAttribute("name", inputId, null);
//        if(menu.getOnchange() != null) writer.writeAttribute("onchange", menu.getOnchange(), null);
//        if(menu.isDisabled()) writer.writeAttribute("disabled", "disabled", null);
//
//        encodeSelectItems(context, menu, selectItems, type);
//
//        writer.endElement("select");
//
//        writer.endElement("div");
//    }
//
//    protected void encodeLabel(FacesContext context, SelectOneMenu menu, List<SelectItem> selectItems, Class<?> type) throws IOException {
//        ResponseWriter writer = context.getResponseWriter();
//        String label = getSelectedLabel(context, menu, selectItems, type);
//        
//        writer.startElement("a", null);
//        writer.writeAttribute("href", "#", null);
//// writer.writeAttribute("class", SelectOneMenu.LABEL_CONTAINER_CLASS, null);
//        
//        if(menu.getTabindex() != null)
//            writer.writeAttribute("tabindex", menu.getTabindex(), null);
//        
//        writer.startElement("label", null);
//// writer.writeAttribute("class", SelectOneMenu.LABEL_CLASS, null);
//
//        if(label.equals("&nbsp;"))
//            writer.write(label);
//        else
//            writer.writeText(label, null);
//
//        writer.endElement("label");
//        writer.endElement("a");
//    }
//
//    protected void encodeMenuIcon(FacesContext context, SelectOneMenu menu) throws IOException {
//        ResponseWriter writer = context.getResponseWriter();
//        
//        writer.startElement("div", menu);
//// writer.writeAttribute("class", SelectOneMenu.TRIGGER_CLASS, null);
//
//        writer.startElement("span", menu);
//        writer.writeAttribute("class", "ui-icon ui-icon-triangle-1-s", null);
//        writer.endElement("span");
//
//        writer.endElement("div");
//    }
//
//    protected void encodePanel(FacesContext context, SelectOneMenu menu, List<SelectItem> selectItems, Class<?> type) throws IOException {
//        ResponseWriter writer = context.getResponseWriter();
//        boolean customContent = false;//menu.getVar() != null;
//        int height = 200;//calculatePanelHeight(menu, selectItems.size());
//
//        writer.startElement("div", null);
//        writer.writeAttribute("id", menu.getClientId(context) + "_panel", null);
//// writer.writeAttribute("class", SelectOneMenu.PANEL_CLASS, null);
//        
//        if(height != -1) {
//            writer.writeAttribute("style", "height:" + height + "px", null);
//        }
//
//        if(customContent) {
//            writer.startElement("table", menu);
//// writer.writeAttribute("class", SelectOneMenu.TABLE_CLASS, null);
//            writer.startElement("tbody", menu);
//// encodeOptionsAsTable(context, menu, selectItems, type);
//            writer.endElement("tbody");
//            writer.endElement("table");
//        } else {
//            writer.startElement("ul", menu);
//// writer.writeAttribute("class", SelectOneMenu.LIST_CLASS, null);
//            encodeOptionsAsList(context, menu, selectItems, type);
//            writer.endElement("ul");
//        }
//        
//        writer.endElement("div");
//    }
//
//// protected void encodeOptionsAsTable(FacesContext context, SelectOneMenu menu, List<SelectItem> selectItems, Class type) throws IOException {
//// ResponseWriter writer = context.getResponseWriter();
//// String var = menu.getVar();
//// List<Column> columns = menu.getColums();
//// Object value = menu.getValue();
////
//// for(SelectItem selectItem : selectItems) {
//// Object itemValue = selectItem.getValue();
//// Object coercedItemValue = null;
////
//// if(itemValue != null && !itemValue.equals("")) {
//// coercedItemValue = context.getApplication().getExpressionFactory().coerceToType(itemValue, type);
//// }
////
//// boolean selected = (value != null && value.equals(coercedItemValue));
////
//// context.getExternalContext().getRequestMap().put(var, selectItem.getValue());
////
//// String rowStyleClass = selected ? SelectOneMenu.ROW_CLASS + " ui-state-active" : SelectOneMenu.ROW_CLASS;
////
//// writer.startElement("tr", null);
//// writer.writeAttribute("class", rowStyleClass, null);
////
//// if(itemValue instanceof String) {
//// writer.startElement("td", null);
//// writer.writeAttribute("colspan", columns.size(), null);
//// writer.write(selectItem.getLabel());
//// writer.endElement("td");
//// } else {
//// for(Column column : columns) {
//// writer.startElement("td", null);
//// column.encodeAll(context);
//// writer.endElement("td");
//// }
//// }
////
//// writer.endElement("tr");
//// }
////
//// context.getExternalContext().getRequestMap().put(var, null);
//// }
//
//    protected void encodeOptionsAsList(FacesContext context, SelectOneMenu menu, List<SelectItem> selectItems, Class<?> type) throws IOException {
//        ResponseWriter writer = context.getResponseWriter();
//// Object value = menu.getValue();
//
//        for(int i = 0; i < selectItems.size(); i++) {
//            SelectItem selectItem = selectItems.get(i);
//// Object itemValue = selectItem.getValue();
//            String itemLabel = selectItem.getLabel();
//// Object coercedItemValue = null;
//            itemLabel = isValueBlank(itemLabel) ? "&nbsp;" : itemLabel;
//            
//// if(itemValue != null && !itemValue.equals("")) {
//// coercedItemValue = context.getApplication().getExpressionFactory().coerceToType(itemValue, type);
//// }
//
//// boolean selected = (i==0 && value==null) || (value != null && value.equals(coercedItemValue));
//// String itemStyleClass = selected ? SelectOneMenu.ITEM_CLASS + " ui-state-active" : SelectOneMenu.ITEM_CLASS;
//            
//            writer.startElement("li", null);
//// writer.writeAttribute("class", itemStyleClass, null);
//            
//            if(itemLabel.equals("&nbsp;"))
//                writer.write(itemLabel);
//            else
//                writer.writeText(itemLabel, null);
//
//            writer.endElement("li");
//        }
//    }
//
//    protected void encodeSelectItems(FacesContext context, SelectOneMenu menu, List<SelectItem> selectItems, Class<?> type) throws IOException {
//        ResponseWriter writer = context.getResponseWriter();
//        Converter converter = getConverter(context, menu);
//        Object value = menu.getValue();
//        
//        for(SelectItem selectItem : selectItems) {
//            Object itemValue = selectItem.getValue();
//            String itemLabel = selectItem.getLabel();
//            
//            if(itemValue != null && !itemValue.equals("")) {
//                itemValue = context.getApplication().getExpressionFactory().coerceToType(itemValue, type);
//            }
//
//            writer.startElement("option", null);
//            writer.writeAttribute("value", getOptionAsString(context, menu, converter, itemValue), null);
//
//            if(value != null && value.equals(itemValue)) {
//                writer.writeAttribute("selected", "selected", null);
//            }
//
//            writer.write(itemLabel);
//
//            writer.endElement("option");
//        }
//    }
//
//protected String getSelectedLabel(FacesContext context, SelectOneMenu menu, List<SelectItem> items, Class<?> type) {
//Object value = menu.getValue();
//        String label = null;
//        
//        for(SelectItem item : items) {
//            Object itemValue = item.getValue();
//            if(itemValue != null && !itemValue.equals("")) {
//                itemValue = context.getApplication().getExpressionFactory().coerceToType(item.getValue(), type);
//            }
//
//            if(value != null && value.equals(itemValue)) {
//                label = item.getLabel();
//                break;
//            }
//        }
//
//        if(label == null) {
//            label = !items.isEmpty() ? items.get(0).getLabel() : "&nbsp;";
//        }
// 
//        return label;
//}
//    
//// protected int calculatePanelHeight(SelectOneMenu menu, int itemSize) {
//// int height = menu.getHeight();
////
//// if(height != Integer.MAX_VALUE) {
//// return height;
//// } else if(itemSize > 10) {
//// return 200;
//// }
////
//// return -1;
//// }
//
//    @Override
//    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
////Rendering happens on encodeEnd
//}
//
//    @Override
//public boolean getRendersChildren() {
//return true;
//}
//    
//    protected Class<?> getValueType(FacesContext context, SelectOneMenu menu) {
//        ValueExpression ve = menu.getValueExpression("value");
//        Class<?> type = ve == null ? String.class : ve.getType(context.getELContext());
//        
//        return type == null ? String.class : type;
//    }
//}
}