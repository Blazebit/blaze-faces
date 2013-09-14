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

package com.blazebit.blazefaces.behavior.ajax;

import javax.faces.component.behavior.ClientBehaviorHint;
import javax.el.ELContext;
import java.util.EnumSet;
import java.util.Map;
import javax.faces.FacesException;
import javax.el.ValueExpression;
import javax.faces.component.UIComponentBase;
import java.util.Collections;
import javax.el.ELException;
import java.util.Set;
import java.util.HashMap;
import javax.faces.context.FacesContext;

public abstract class AjaxBehaviorBase extends javax.faces.component.behavior.ClientBehaviorBase {

    public static final String BEHAVIOR_ID = "com.blazebit.blazefaces.behavior.AjaxBehavior";
    private static final String DEFAULT_RENDERER = "com.blazebit.blazefaces.behavior.renderer.AjaxBehaviorRenderer";
    private static final Set<ClientBehaviorHint> HINTS = Collections.unmodifiableSet(EnumSet.of(ClientBehaviorHint.SUBMITTING));
    
    protected javax.el.MethodExpression listener;
    protected java.lang.Boolean async;
    protected java.lang.String process;
    protected java.lang.String update;
    protected java.lang.String onstart;
    protected java.lang.String oncomplete;
    protected java.lang.String onerror;
    protected java.lang.String onsuccess;
    protected java.lang.Boolean immediate;
    protected java.lang.Boolean global;
    protected java.lang.Boolean disabled;
    protected java.lang.String event;
    protected java.lang.Boolean partialSubmit;
    protected Map<String, ValueExpression> bindings;

    @Override
    public String getRendererType() {
        return DEFAULT_RENDERER;
    }

    @Override
    public Set<ClientBehaviorHint> getHints() {
        return HINTS;
    }

    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }
        
    public javax.el.MethodExpression getListener() {
        javax.el.MethodExpression result = (javax.el.MethodExpression) eval("listener", listener);
        return ((result != null) ? result : null);
    }
    
    public void setListener(javax.el.MethodExpression listener) {
        this.listener = listener;
        clearInitialState();
    }
    
    public boolean isAsync() {
        java.lang.Boolean result = (java.lang.Boolean) eval("async", async);
        return ((result != null) ? result : false);
    }
    
    public void setAsync(boolean async) {
        this.async = async;
        clearInitialState();
    }
    
    public java.lang.String getProcess() {
        java.lang.String result = (java.lang.String) eval("process", process);
        return ((result != null) ? result : "@this");
    }
    
    public void setProcess(java.lang.String process) {
        this.process = process;
        clearInitialState();
    }
    
    public java.lang.String getUpdate() {
        java.lang.String result = (java.lang.String) eval("update", update);
        return ((result != null) ? result : null);
    }
    
    public void setUpdate(java.lang.String update) {
        this.update = update;
        clearInitialState();
    }
    
    public java.lang.String getOnstart() {
        java.lang.String result = (java.lang.String) eval("onstart", onstart);
        return ((result != null) ? result : null);
    }
    
    public void setOnstart(java.lang.String onstart) {
        this.onstart = onstart;
        clearInitialState();
    }
    
    public java.lang.String getOncomplete() {
        java.lang.String result = (java.lang.String) eval("oncomplete", oncomplete);
        return ((result != null) ? result : null);
    }
    
    public void setOncomplete(java.lang.String oncomplete) {
        this.oncomplete = oncomplete;
        clearInitialState();
    }
    
    public java.lang.String getOnerror() {
        java.lang.String result = (java.lang.String) eval("onerror", onerror);
        return ((result != null) ? result : null);
    }
    
    public void setOnerror(java.lang.String onerror) {
        this.onerror = onerror;
        clearInitialState();
    }
    
    public java.lang.String getOnsuccess() {
        java.lang.String result = (java.lang.String) eval("onsuccess", onsuccess);
        return ((result != null) ? result : null);
    }
    
    public void setOnsuccess(java.lang.String onsuccess) {
        this.onsuccess = onsuccess;
        clearInitialState();
    }
    
    public boolean isImmediate() {
        java.lang.Boolean result = (java.lang.Boolean) eval("immediate", immediate);
        return ((result != null) ? result : false);
    }
    
    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
        clearInitialState();
    }
    
    public boolean isGlobal() {
        java.lang.Boolean result = (java.lang.Boolean) eval("global", global);
        return ((result != null) ? result : false);
    }
    
    public void setGlobal(boolean global) {
        this.global = global;
        clearInitialState();
    }
    
    public boolean isDisabled() {
        java.lang.Boolean result = (java.lang.Boolean) eval("disabled", disabled);
        return ((result != null) ? result : false);
    }
    
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        clearInitialState();
    }
    
    public java.lang.String getEvent() {
        java.lang.String result = (java.lang.String) eval("event", event);
        return ((result != null) ? result : null);
    }
    
    public void setEvent(java.lang.String event) {
        this.event = event;
        clearInitialState();
    }
    
    public boolean isPartialSubmit() {
        java.lang.Boolean result = (java.lang.Boolean) eval("partialSubmit", partialSubmit);
        return ((result != null) ? result : false);
    }
    
    public void setPartialSubmit(boolean partialSubmit) {
        this.partialSubmit = partialSubmit;
        clearInitialState();
    }
    
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
        
         if ("listener".equals(propertyName)) {
            listener = (javax.el.MethodExpression) value;
        }
         else if ("async".equals(propertyName)) {
            async = (java.lang.Boolean) value;
        }
         else if ("process".equals(propertyName)) {
            process = (java.lang.String) value;
        }
         else if ("update".equals(propertyName)) {
            update = (java.lang.String) value;
        }
         else if ("onstart".equals(propertyName)) {
            onstart = (java.lang.String) value;
        }
         else if ("oncomplete".equals(propertyName)) {
            oncomplete = (java.lang.String) value;
        }
         else if ("onerror".equals(propertyName)) {
            onerror = (java.lang.String) value;
        }
         else if ("onsuccess".equals(propertyName)) {
            onsuccess = (java.lang.String) value;
        }
         else if ("immediate".equals(propertyName)) {
            immediate = (java.lang.Boolean) value;
        }
         else if ("global".equals(propertyName)) {
            global = (java.lang.Boolean) value;
        }
         else if ("disabled".equals(propertyName)) {
            disabled = (java.lang.Boolean) value;
        }
         else if ("event".equals(propertyName)) {
            event = (java.lang.String) value;
        }
         else if ("partialSubmit".equals(propertyName)) {
            partialSubmit = (java.lang.Boolean) value;
        }
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
            values = new Object[15];
      
            values[0] = superState;
            values[1] = listener;
            values[2] = async;
            values[3] = process;
            values[4] = update;
            values[5] = onstart;
            values[6] = oncomplete;
            values[7] = onerror;
            values[8] = onsuccess;
            values[9] = immediate;
            values[10] = global;
            values[11] = disabled;
            values[12] = event;
            values[13] = partialSubmit;
            values[14] = saveBindings(context, bindings);
        }

        return values;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        if(state != null) {
            Object[] values = (Object[]) state;
            super.restoreState(context, values[0]);

            if(values.length != 1) {
                listener = (javax.el.MethodExpression) values[1];
                async = (java.lang.Boolean) values[2];
                process = (java.lang.String) values[3];
                update = (java.lang.String) values[4];
                onstart = (java.lang.String) values[5];
                oncomplete = (java.lang.String) values[6];
                onerror = (java.lang.String) values[7];
                onsuccess = (java.lang.String) values[8];
                immediate = (java.lang.Boolean) values[9];
                global = (java.lang.Boolean) values[10];
                disabled = (java.lang.Boolean) values[11];
                event = (java.lang.String) values[12];
                partialSubmit = (java.lang.Boolean) values[13];
                bindings = restoreBindings(context, values[14]);

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