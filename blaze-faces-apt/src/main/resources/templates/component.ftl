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

package ${component.package};

<#list component.imports as import>
import ${import};
</#list>

public abstract class ${component.shortName} extends ${component.parent} {

        public static final String COMPONENT_TYPE = "${component.type}";
        public static final String COMPONENT_FAMILY = "${component.family}";
        <#if component.rendererType??>
        private static final String DEFAULT_RENDERER = "${component.rendererType}";
        </#if>
        private static final String OPTIMIZED_PACKAGE = "com.blazebit.blazefaces.component.";
        
        protected enum PropertyKeys {
            <#if !component.tag.effectiveAttributes.isEmpty()>
            <#list component.tag.effectiveAttributes as attribute>
            ${attribute.name}<#if attribute_has_next>,<#else>;</#if>
            </#list>
            <#else>
            ;
            </#if>

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
        
        public ${component.shortName}() {
        <#if component.rendererType??>
            setRendererType(DEFAULT_RENDERER);
        <#else>
            setRendererType(null);
        </#if>
        }
        
        @Override
        public String getFamily() {
            return COMPONENT_FAMILY;
        }
        
        <#list component.tag.effectiveAttributes as attribute>
        <#if attribute.type == "boolean">
        public ${attribute.type} is${attribute.capitalizedName}() {
            <#if attribute.defaultValue == "null">
            return (${attribute.objectType}) getStateHelper().eval(PropertyKeys.${attribute.name}, false);
            <#else>
            return (${attribute.objectType}) getStateHelper().eval(PropertyKeys.${attribute.name}, ${attribute.defaultValue});
            </#if>
        }
        <#else>
        public ${attribute.type} get${attribute.capitalizedName}() {
            <#if attribute.defaultValue == "null">
            return (${attribute.objectType}) getStateHelper().eval(PropertyKeys.${attribute.name}, null);
            <#else>
            <#if attribute.type == "java.lang.String">
            return (${attribute.objectType}) getStateHelper().eval(PropertyKeys.${attribute.name}, "${attribute.defaultValue}");
            <#else>
            return (${attribute.objectType}) getStateHelper().eval(PropertyKeys.${attribute.name}, ${attribute.defaultValue});
            </#if>
            </#if>
        }
        </#if>
        
        public void set${attribute.capitalizedName}(${attribute.type} _${attribute.name}) {
            getStateHelper().put(PropertyKeys.${attribute.name}, _${attribute.name});
            handleAttribute("${attribute.name}", _${attribute.name});
        }

        </#list>
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