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

package com.blazebit.blazefaces.component.outputtime;

import java.util.ArrayList;
import javax.el.ValueExpression;
import java.util.List;

public abstract class OutputTimeBase extends javax.faces.component.UIOutput {

        public static final String COMPONENT_TYPE = "com.blazebit.blazefaces.component.OutputTime";
        public static final String COMPONENT_FAMILY = "com.blazebit.blazefaces.component";
        private static final String DEFAULT_RENDERER = "com.blazebit.blazefaces.renderer.OutputTimeRenderer";
        private static final String OPTIMIZED_PACKAGE = "com.blazebit.blazefaces.component.";
        
        protected enum PropertyKeys {
            pubdate,
            pretty,
            pattern,
            timeZone,
            locale,
            type,
            dateStyle,
            timeStyle,
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
        
        public OutputTimeBase() {
            setRendererType(DEFAULT_RENDERER);
        }
        
        @Override
        public String getFamily() {
            return COMPONENT_FAMILY;
        }
        
        public java.lang.Boolean getPubdate() {
            return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.pubdate, false);
        }
        
        public void setPubdate(java.lang.Boolean _pubdate) {
            getStateHelper().put(PropertyKeys.pubdate, _pubdate);
            handleAttribute("pubdate", _pubdate);
        }

        public java.lang.Boolean getPretty() {
            return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.pretty, false);
        }
        
        public void setPretty(java.lang.Boolean _pretty) {
            getStateHelper().put(PropertyKeys.pretty, _pretty);
            handleAttribute("pretty", _pretty);
        }

        public java.lang.String getPattern() {
            return (java.lang.String) getStateHelper().eval(PropertyKeys.pattern, null);
        }
        
        public void setPattern(java.lang.String _pattern) {
            getStateHelper().put(PropertyKeys.pattern, _pattern);
            handleAttribute("pattern", _pattern);
        }

        public java.util.TimeZone getTimeZone() {
            return (java.util.TimeZone) getStateHelper().eval(PropertyKeys.timeZone, null);
        }
        
        public void setTimeZone(java.util.TimeZone _timeZone) {
            getStateHelper().put(PropertyKeys.timeZone, _timeZone);
            handleAttribute("timeZone", _timeZone);
        }

        public java.util.Locale getLocale() {
            return (java.util.Locale) getStateHelper().eval(PropertyKeys.locale, null);
        }
        
        public void setLocale(java.util.Locale _locale) {
            getStateHelper().put(PropertyKeys.locale, _locale);
            handleAttribute("locale", _locale);
        }

        public java.lang.String getType() {
            return (java.lang.String) getStateHelper().eval(PropertyKeys.type, "date");
        }
        
        public void setType(java.lang.String _type) {
            getStateHelper().put(PropertyKeys.type, _type);
            handleAttribute("type", _type);
        }

        public java.lang.String getDateStyle() {
            return (java.lang.String) getStateHelper().eval(PropertyKeys.dateStyle, "default");
        }
        
        public void setDateStyle(java.lang.String _dateStyle) {
            getStateHelper().put(PropertyKeys.dateStyle, _dateStyle);
            handleAttribute("dateStyle", _dateStyle);
        }

        public java.lang.String getTimeStyle() {
            return (java.lang.String) getStateHelper().eval(PropertyKeys.timeStyle, "default");
        }
        
        public void setTimeStyle(java.lang.String _timeStyle) {
            getStateHelper().put(PropertyKeys.timeStyle, _timeStyle);
            handleAttribute("timeStyle", _timeStyle);
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