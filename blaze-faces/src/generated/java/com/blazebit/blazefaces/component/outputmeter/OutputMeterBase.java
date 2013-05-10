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

package com.blazebit.blazefaces.component.outputmeter;

import java.util.ArrayList;
import javax.el.ValueExpression;
import java.util.List;

public abstract class OutputMeterBase extends javax.faces.component.UIOutput {

        public static final String COMPONENT_TYPE = "com.blazebit.blazefaces.component.OutputMeter";
        public static final String COMPONENT_FAMILY = "com.blazebit.blazefaces.component";
        private static final String DEFAULT_RENDERER = "com.blazebit.blazefaces.renderer.OutputMeterRenderer";
        private static final String OPTIMIZED_PACKAGE = "com.blazebit.blazefaces.component.";
        
        protected enum PropertyKeys {
            min,
            max,
            low,
            high,
            optimum,
            style,
            styleClass;

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
        
        public OutputMeterBase() {
            setRendererType(DEFAULT_RENDERER);
        }
        
        @Override
        public String getFamily() {
            return COMPONENT_FAMILY;
        }
        
        public java.lang.Double getMin() {
            return (java.lang.Double) getStateHelper().eval(PropertyKeys.min, 0);
        }
        
        public void setMin(java.lang.Double _min) {
            getStateHelper().put(PropertyKeys.min, _min);
            handleAttribute("min", _min);
        }

        public java.lang.Double getMax() {
            return (java.lang.Double) getStateHelper().eval(PropertyKeys.max, 1);
        }
        
        public void setMax(java.lang.Double _max) {
            getStateHelper().put(PropertyKeys.max, _max);
            handleAttribute("max", _max);
        }

        public java.lang.Double getLow() {
            return (java.lang.Double) getStateHelper().eval(PropertyKeys.low, getMin());
        }
        
        public void setLow(java.lang.Double _low) {
            getStateHelper().put(PropertyKeys.low, _low);
            handleAttribute("low", _low);
        }

        public java.lang.Double getHigh() {
            return (java.lang.Double) getStateHelper().eval(PropertyKeys.high, getMax());
        }
        
        public void setHigh(java.lang.Double _high) {
            getStateHelper().put(PropertyKeys.high, _high);
            handleAttribute("high", _high);
        }

        public java.lang.Double getOptimum() {
            return (java.lang.Double) getStateHelper().eval(PropertyKeys.optimum, null);
        }
        
        public void setOptimum(java.lang.Double _optimum) {
            getStateHelper().put(PropertyKeys.optimum, _optimum);
            handleAttribute("optimum", _optimum);
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