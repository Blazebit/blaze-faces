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
package com.blazebit.blazefaces.mobile.renderkit;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import com.blazebit.blazefaces.renderkit.InputRenderer;
import com.blazebit.blazefaces.util.ComponentUtils;
import com.blazebit.blazefaces.util.HTML;
import com.blazebit.blazefaces.component.inputtext.InputText;
import com.blazebit.blazefaces.mobile.util.MobileUtils;

public class InputTextRenderer extends InputRenderer {

    @Override
	public void decode(FacesContext context, UIComponent component) {
		InputText inputText = (InputText) component;
        String clientId = inputText.getClientId(context);
        String inputId = inputText.getLabel() == null ? clientId : clientId + "_input";

        if(inputText.isDisabled() || inputText.isReadonly()) {
            return;
        }

        decodeBehaviors(context, inputText);

		String submittedValue = (String) context.getExternalContext().getRequestParameterMap().get(inputId);

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
        String label = inputText.getLabel();
        String inputId = label == null ? clientId : clientId + "_input";

        if(label == null) {
            encodeInput(context, inputText, inputId);
        } 
        else {
            writer.startElement("div", inputText);
            writer.writeAttribute("id", clientId, null);
            writer.writeAttribute("data-role", "fieldcontain", null);
                        
            writer.startElement("label", null);
            writer.writeAttribute("for", inputId, null);
            writer.writeText(label, "label");
            writer.endElement("label");
            
            encodeInput(context, inputText, inputId);
            
            writer.endElement("div");
        }
	}
     
    protected void encodeInput(FacesContext context, InputText inputText, String inputId) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String type = inputText.getType();
        boolean isSearch = type.equals("search");
        
        //degrade
        if(isSearch) {
            type = "text";
        }

        writer.startElement("input", null);
		writer.writeAttribute("id", inputId, null);
		writer.writeAttribute("name", inputId, null);
		writer.writeAttribute("type", type, null);
        
		String valueToRender = ComponentUtils.getValueToRender(context, inputText);
		if(valueToRender != null) {
			writer.writeAttribute("value", valueToRender , null);
		}

		renderPassThruAttributes(context, inputText, HTML.INPUT_TEXT_ATTRS);

        if(isSearch) writer.writeAttribute("data-type", "search", null);
        if(MobileUtils.isMini(context)) writer.writeAttribute("data-mini", "true", null);
        if(inputText.isDisabled()) writer.writeAttribute("disabled", "disabled", null);
        if(inputText.isReadonly()) writer.writeAttribute("readonly", "readonly", null);
        if(inputText.getStyle() != null) writer.writeAttribute("style", inputText.getStyle(), null);
        if(inputText.getStyleClass() != null) writer.writeAttribute("class", inputText.getStyleClass(), "styleClass");

        writer.endElement("input");
    }
}