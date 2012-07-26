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
package com.blazebit.blazefaces.component.inputtext;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import com.blazebit.blazefaces.renderkit.InputRenderer;
import com.blazebit.blazefaces.util.ComponentUtils;
import com.blazebit.blazefaces.util.HTML;

public class InputTextRenderer extends InputRenderer {

    @Override
	public void decode(FacesContext context, UIComponent component) {
		InputText inputText = (InputText) component;

        if(inputText.isDisabled() || inputText.isReadonly()) {
            return;
        }

        decodeBehaviors(context, inputText);

		String clientId = inputText.getClientId(context);
		String submittedValue = (String) context.getExternalContext().getRequestParameterMap().get(clientId);

        if(submittedValue != null) {
            inputText.setSubmittedValue(submittedValue);
        }
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		InputText inputText = (InputText) component;

		encodeMarkup(context, inputText);
		encodeScript(context, inputText);
	}

	protected void encodeScript(FacesContext context, InputText inputText) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = inputText.getClientId(context);

        startScript(writer, clientId);
        
        writer.write("BlazeFaces.cw('InputText','" + inputText.resolveWidgetVar() + "',{");
        writer.write("id:'" + clientId + "'");

        encodeClientBehaviors(context, inputText);

        writer.write("});");

		endScript(writer);
	}

	protected void encodeMarkup(FacesContext context, InputText inputText) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = inputText.getClientId(context);

		writer.startElement("input", null);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("name", clientId, null);
		writer.writeAttribute("type", inputText.getType(), null);

		String valueToRender = ComponentUtils.getValueToRender(context, inputText);
		if(valueToRender != null) {
			writer.writeAttribute("value", valueToRender , null);
		}

		renderPassThruAttributes(context, inputText, HTML.INPUT_TEXT_ATTRS);

        if(inputText.isDisabled()) writer.writeAttribute("disabled", "disabled", null);
        if(inputText.isReadonly()) writer.writeAttribute("readonly", "readonly", null);
        if(inputText.getStyle() != null) writer.writeAttribute("style", inputText.getStyle(), null);

        writer.writeAttribute("class", createStyleClass(inputText), "styleClass");

        writer.endElement("input");
	}

    protected String createStyleClass(InputText inputText) {
        String defaultClass = InputText.STYLE_CLASS;
        defaultClass = inputText.isValid() ? defaultClass : defaultClass + " ui-state-error";
        defaultClass = !inputText.isDisabled() ? defaultClass : defaultClass + " ui-state-disabled";
        
        String styleClass = inputText.getStyleClass();
        styleClass = styleClass == null ? defaultClass : defaultClass + " " + styleClass;
        
        return styleClass;
    }
    
    // Old implementation
    // TODO: merge code
//    private static final String DEFAULT_TYPE = "text";
//    private static final String[] ALLOWED_TYPES = {
//        "text",
//        "password",
//        "hidden"
//    };
//    
//    @Override
//    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
//        ResponseWriter writer = context.getResponseWriter();
//        String clientId = component.getClientId(context);
//        String value = getCurrentValue(context, component);
//        String type = getType(context, component);
//        
//        writer.startElement("input", component);
//        RendererUtils.encodeAttribute(writer, "id", clientId, null);
//        RendererUtils.encodeAttribute(writer, "name", clientId, null);
//        RendererUtils.encodeAttribute(writer, "type", type, null);
//        RendererUtils.encodeAttribute(writer, "class", component.getAttributes().get("styleClass"), null);
//        RendererUtils.encodeAttribute(writer, "value", value, null);
//        renderPassThruAttributes(context, component, HTML5.COMMON_ATTRIBUTES);
//        writer.endElement("input");
//        encodeBehaviors(context, (ClientBehaviorHolder) component);
//    }
//
//    private String getType(FacesContext context, UIComponent component) {
//        String inputType = (String) component.getAttributes().get("type");
//        
//        for(String type : ALLOWED_TYPES){
//            if(type.equalsIgnoreCase(inputType))
//                return type;
//        }
//        
//        return DEFAULT_TYPE;
//    }
}