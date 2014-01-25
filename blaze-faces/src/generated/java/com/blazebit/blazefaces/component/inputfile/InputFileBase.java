/*
 * Generated, Do Not Modify
 */
/*
 *
 * Copyright 2013 Blazebit.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blazebit.blazefaces.component.inputfile;

import java.util.ArrayList;
import javax.el.ValueExpression;
import java.util.List;

public abstract class InputFileBase extends javax.faces.component.UIInput {

        public static final String COMPONENT_TYPE = "com.blazebit.blazefaces.component.InputFile";
        public static final String COMPONENT_FAMILY = "com.blazebit.blazefaces.component";
        private static final String DEFAULT_RENDERER = "com.blazebit.blazefaces.renderer.InputFileRenderer";
        private static final String OPTIMIZED_PACKAGE = "com.blazebit.blazefaces.component.";
        
        protected enum PropertyKeys {
            style,
            styleClass,
            allowedExtensions,
            maxFileSize,
            disabled;

            String toString;

            PropertyKeys(String toString) {
                this.toString = toString;
            }

            PropertyKeys() {}

            @Override
            public String toString() {
                return ((this.toString != null) ? this.toString : super.toString());
            }
        }
        
        public InputFileBase() {
            setRendererType(DEFAULT_RENDERER);
        }
        
        @Override
        public String getFamily() {
            return COMPONENT_FAMILY;
        }
        
        public java.lang.String getStyle() {
            return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
        }
        
        public void setStyle(java.lang.String _style) {
            getStateHelper().put(PropertyKeys.style, _style);
            handleAttribute("style", _style);
        }

        public java.lang.String getStyleClass() {
            return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
        }
        
        public void setStyleClass(java.lang.String _styleClass) {
            getStateHelper().put(PropertyKeys.styleClass, _styleClass);
            handleAttribute("styleClass", _styleClass);
        }

        public java.lang.String getAllowedExtensions() {
            return (java.lang.String) getStateHelper().eval(PropertyKeys.allowedExtensions, null);
        }
        
        public void setAllowedExtensions(java.lang.String _allowedExtensions) {
            getStateHelper().put(PropertyKeys.allowedExtensions, _allowedExtensions);
            handleAttribute("allowedExtensions", _allowedExtensions);
        }

        public long getMaxFileSize() {
            return (java.lang.Long) getStateHelper().eval(PropertyKeys.maxFileSize, 0);
        }
        
        public void setMaxFileSize(long _maxFileSize) {
            getStateHelper().put(PropertyKeys.maxFileSize, _maxFileSize);
            handleAttribute("maxFileSize", _maxFileSize);
        }

        public boolean isDisabled() {
            return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.disabled, false);
        }
        
        public void setDisabled(boolean _disabled) {
            getStateHelper().put(PropertyKeys.disabled, _disabled);
            handleAttribute("disabled", _disabled);
        }

        @SuppressWarnings("unchecked")
        public void handleAttribute(String name, Object value) {
            List<String> setAttributes = (List<String>) this.getAttributes().get("javax.faces.component.UIComponentBase.attributesThatAreSet");
            if(setAttributes == null) {
                String cname = this.getClass().getName();
                if(cname != null && cname.startsWith(OPTIMIZED_PACKAGE)) {
                    setAttributes = new ArrayList<String>(6);
                    this.getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
                }
            }
            if(setAttributes != null) {
                if(value == null) {
                    ValueExpression ve = getValueExpression(name);
                    if(ve == null) {
                        setAttributes.remove(name);
                    } else if(!setAttributes.contains(name)) {
                        setAttributes.add(name);
                    }
                }
            }
        }
}