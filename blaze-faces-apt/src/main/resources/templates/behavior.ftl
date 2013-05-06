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

package ${behavior.package};

<#list behavior.imports as import>
import ${import};
</#list>

public abstract class ${behavior.shortName} extends ${behavior.parent} {

    public static final String BEHAVIOR_ID = "${behavior.id}";
    <#if behavior.hints.size() != 0>
    private static final Set<ClientBehaviorHint> HINTS = Collections.unmodifiableSet(EnumSet.of(<#list behavior.hints as hint>${hint}<#if hint_has_next>, </#if></#list>));
    </#if>
    
    <#list behavior.tag.effectiveAttributes as attribute>
    protected ${attribute.objectType} ${attribute.name};
    </#list>
    protected Map<String, ValueExpression> bindings;

    @Override
    public String getRendererType() {
        return BEHAVIOR_ID;
    }

    <#if behavior.hints.size() != 0>
    @Override
    public Set<ClientBehaviorHint> getHints() {
        return HINTS;
    }
    </#if>

    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }
        
<#list behavior.tag.effectiveAttributes as attribute>
	<#if attribute.type == "boolean">
    public ${attribute.type} is${attribute.capitalizedName}() {
        ${attribute.objectType} result = (${attribute.objectType}) eval("${attribute.name}", ${attribute.name});
        <#if attribute.defaultValue == "null">
        return ((result != null) ? result : false);
        <#else>
        <#if attribute.type == "java.lang.String">
        return ((result != null) ? result : "${attribute.defaultValue}");
        <#else>
        return ((result != null) ? result : ${attribute.defaultValue});
        </#if>
        </#if>
    }
    <#else>
    public ${attribute.type} get${attribute.capitalizedName}() {
        ${attribute.objectType} result = (${attribute.objectType}) eval("${attribute.name}", ${attribute.name});
        <#if attribute.defaultValue == "null">
        return ((result != null) ? result : null);
        <#else>
        <#if attribute.type == "java.lang.String">
        return ((result != null) ? result : "${attribute.defaultValue}");
        <#else>
        return ((result != null) ? result : ${attribute.defaultValue});
        </#if>
        </#if>
    }
    </#if>
    
    public void set${attribute.capitalizedName}(${attribute.type} ${attribute.name}) {
        this.${attribute.name} = ${attribute.name};
        clearInitialState();
    }
    
</#list>
    protected Object eval(String propertyName, Object value) {
        if(value != null) {
            return value;
        }

        ValueExpression expression = getValueExpression(propertyName);
        
        if(expression != null) {
            return expression.getValue(FacesContext.getCurrentInstance().getELContext());
        }

        return null;
    }
    
    public ValueExpression getValueExpression(String name) {
        if(name == null) {
            throw new IllegalArgumentException();
        }

        return ((bindings == null) ? null : bindings.get(name));
    }
    
    public void setValueExpression(String name, ValueExpression expr) {
        if(name == null) {
            throw new IllegalArgumentException();
        }

        if(expr != null) {
            if(expr.isLiteralText()) {
                setLiteralValue(name, expr);
            } else {
                if(bindings == null) {
                    bindings = new HashMap<String, ValueExpression>(6,1.0f);
                }

                bindings.put(name, expr);
            }
        } 
        else {
            if(bindings != null) {
                bindings.remove(name);
                
                if(bindings.isEmpty()) {
                    bindings = null;
                }
            }
        }

        clearInitialState();
    }
    
    protected void setLiteralValue(String propertyName, ValueExpression expression) {
        Object value;
        ELContext context = FacesContext.getCurrentInstance().getELContext();

        try {
            value = expression.getValue(context);
        } 
        catch (ELException eLException) {
            throw new FacesException(eLException);
        }
        
        <#list behavior.tag.effectiveAttributes as attribute>
        <#if attribute_index != 0> else</#if> if ("${attribute.name}".equals(propertyName)) {
            ${attribute.name} = (${attribute.objectType}) value;
        }
        </#list>
    }
    
    @Override
    public Object saveState(FacesContext context) {
        Object[] values;

        Object superState = super.saveState(context);

        if(initialStateMarked()) {
            if(superState == null)
                values = null;
            else
                values = new Object[] {superState};
        } 
        else {
            values = new Object[${behavior.tag.effectiveAttributes.size() + 2}];
      
            values[0] = superState;
            <#list behavior.tag.effectiveAttributes as attribute>
            values[${attribute_index + 1}] = ${attribute.name};
            </#list>
            values[${behavior.tag.effectiveAttributes.size() + 1}] = saveBindings(context, bindings);
        }

        return values;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        if(state != null) {
            Object[] values = (Object[]) state;
            super.restoreState(context, values[0]);

            if(values.length != 1) {
                <#list behavior.tag.effectiveAttributes as attribute>
                ${attribute.name} = (${attribute.objectType}) values[${attribute_index + 1}];
                </#list>
                bindings = restoreBindings(context, values[${behavior.tag.effectiveAttributes.size() + 1}]);

                clearInitialState();
            }
        }
    }
    private Object saveBindings(FacesContext context, Map<String, ValueExpression> bindings) {
        if(bindings == null) {
            return null;
        }

        Object values[] = new Object[2];
        values[0] = bindings.keySet().toArray(new String[bindings.size()]);

        Object[] bindingValues = bindings.values().toArray();
        for (int i = 0; i < bindingValues.length; i++) {
            bindingValues[i] = UIComponentBase.saveAttachedState(context, bindingValues[i]);
        }

        values[1] = bindingValues;

        return values;
    }

    private Map<String, ValueExpression> restoreBindings(FacesContext context, Object state) {
        if(state == null) {
            return null;
        }
        
        Object values[] = (Object[]) state;
        String names[] = (String[]) values[0];
        Object states[] = (Object[]) values[1];
        Map<String, ValueExpression> bindings = new HashMap<String, ValueExpression>(names.length);
        
        for (int i = 0; i < names.length; i++) {
            bindings.put(names[i], (ValueExpression) UIComponentBase.restoreAttachedState(context, states[i]));
        }
        return bindings;
    }
}