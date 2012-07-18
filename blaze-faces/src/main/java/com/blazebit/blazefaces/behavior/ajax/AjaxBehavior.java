/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.behavior.ajax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorListener;

/**
 *
 * @author Christian Beikov
 */
@ResourceDependencies({
    @ResourceDependency(library = "blazefaces", name = "jquery/jquery.js"),
    @ResourceDependency(library = "blazefaces", name = "core/core.js"),
    @ResourceDependency(library = "blazefaces", name = "core/ajax.js")})
public class AjaxBehavior extends ClientBehaviorBase {

    protected static String BEHAVIOR_ID = "com.blazebit.blazefaces.behavior.AjaxBehavior";
    protected static Set<ClientBehaviorHint> HINTS = Collections.unmodifiableSet(EnumSet.of(ClientBehaviorHint.SUBMITTING));
    
    private Boolean global;
    private Boolean async;
    private Boolean immediate;
    private Boolean disabled;
    private List<String> process;
    private List<String> update;
    private String oncomplete;
    private String onerror;
    private String onsuccess;
    private String onstart;
    private Map<String, ValueExpression> bindings;

    @Override
    public String getRendererType() {
        return BEHAVIOR_ID;
    }

    @Override
    public Set<ClientBehaviorHint> getHints() {
        return HINTS;
    }

    public boolean isAsync() {
        return (Boolean) eval(ASYNC, async, false);
    }

    public void setAsync(boolean async) {
        this.async = async;
        clearInitialState();
    }

    public boolean isGlobal() {
        return (Boolean) eval(GLOBAL, global, true);
    }

    public void setGlobal(boolean global) {
        this.global = global;
        clearInitialState();
    }

    public String getOncomplete() {
        return (String) eval(ONCOMPLETE, oncomplete, null);
    }

    public void setOncomplete(String oncomplete) {
        this.oncomplete = oncomplete;
        clearInitialState();
    }

    public String getOnstart() {
        return (String) eval(ONSTART, onstart, null);
    }

    public void setOnstart(String onstart) {
        this.onstart = onstart;
        clearInitialState();
    }

    public String getOnsuccess() {
        return (String) eval(ONSUCCESS, onsuccess, null);
    }

    public void setOnsuccess(String onsuccess) {
        this.onsuccess = onsuccess;
        clearInitialState();
    }

    public String getOnerror() {
        return (String) eval(ONERROR, onerror, null);
    }

    public void setOnerror(String onerror) {
        this.onerror = onerror;
        clearInitialState();
    }

    public Collection<String> getProcess() {
        return getCollectionValue(PROCESS, process);
    }

    public void setProcess(Collection<String> process) {
        this.process = copyToList(process);
        clearInitialState();
    }

    public Collection<String> getUpdate() {
        return getCollectionValue(PROCESS, update);
    }

    public void setUpdate(Collection<String> update) {
        this.update = copyToList(update);
        clearInitialState();
    }

    public boolean isDisabled() {
        return (Boolean) eval(DISABLED, disabled, false);
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        clearInitialState();
    }

    public boolean isImmediate() {
        return (Boolean) eval(IMMEDIATE, immediate, false);
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
        clearInitialState();
    }

    public boolean isImmediateSet() {
        return ((immediate != null) || (getValueExpression(IMMEDIATE) != null));
    }
    
    public ValueExpression getValueExpression(String name) {

        if (name == null) {
            throw new NullPointerException();
        }

        return ((bindings == null) ? null : bindings.get(name));
    }

    public void setValueExpression(String name, ValueExpression binding) {

        if (name == null) {
            throw new NullPointerException();
        }

        if (binding != null) {

            if (binding.isLiteralText()) {
                setLiteralValue(name, binding);
            } else {
                if (bindings == null) {

                    // We use a very small initial capacity on this HashMap.
                    // The goal is not to reduce collisions, but to keep the
                    // memory footprint small.  It is very unlikely that an
                    // an AjaxBehavior would have more than 1 or 2 bound 
                    // properties - and even if more are present, it's okay
                    // if we have some collisions - will still be fast.
                    bindings = new HashMap<String, ValueExpression>(6,1.0f);
                }

                bindings.put(name, binding);
            }
        } else {
            if (bindings != null) {
                bindings.remove(name);
                if (bindings.isEmpty()) {
                    bindings = null;
                }
            }
        }

        clearInitialState();
    }
    
    public void addAjaxBehaviorListener(AjaxBehaviorListener listener) {
        addBehaviorListener(listener);
    }

    public void removeAjaxBehaviorListener(AjaxBehaviorListener listener) {
        removeBehaviorListener(listener);
    }

    @Override
    public Object saveState(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        Object[] values;

        Object superState = super.saveState(context);

        if (initialStateMarked()) {
            if (superState == null) {
                values = null;
            } else {
                values = new Object[] { superState };
            }
        } else {
            values = new Object[12];
      
            values[0] = superState;
            values[1] = onerror;
            values[2] = async;
            values[3] = global;
            values[4] = oncomplete;
            values[5] = onstart;
            values[6] = onsuccess;
            values[7] = disabled;
            values[8] = immediate;
            values[9] = saveList(process);
            values[10] = saveList(update);
            values[11] = saveBindings(context, bindings);
        }

        return values;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (state != null) {

            Object[] values = (Object[]) state;
            super.restoreState(context, values[0]);

            if (values.length != 1) {
                onerror = (String)values[1];
                async = (Boolean)values[2];
                global = (Boolean)values[3];
                oncomplete = (String)values[4];
                onstart = (String)values[5];
                onsuccess = (String)values[6];
                disabled = (Boolean)values[7];
                immediate = (Boolean)values[8];
                process = restoreList(PROCESS, values[9]);
                update = restoreList(UPDATE, values[10]);
                bindings = restoreBindings(context, values[11]);

                // If we saved state last time, save state again next time.
                clearInitialState();
            }
        }
    }

    private static Object saveBindings(FacesContext context,
                                       Map<String, ValueExpression> bindings) {

        if (bindings == null) {
            return (null);
        }

        Object values[] = new Object[2];
        values[0] = bindings.keySet().toArray(new String[bindings.size()]);

        Object[] bindingValues = bindings.values().toArray();
        for (int i = 0; i < bindingValues.length; i++) {
            bindingValues[i] = UIComponentBase.saveAttachedState(context, bindingValues[i]);
        }

        values[1] = bindingValues;

        return (values);
    }

    private static Map<String, ValueExpression> restoreBindings(FacesContext context,
                                                                Object state) {
        if (state == null) {
            return (null);
        }
        Object values[] = (Object[]) state;
        String names[] = (String[]) values[0];
        Object states[] = (Object[]) values[1];
        Map<String, ValueExpression> bindings = new HashMap<String, ValueExpression>(names.length);
        for (int i = 0; i < names.length; i++) {
            bindings.put(names[i],
                    (ValueExpression) UIComponentBase.restoreAttachedState(context, states[i]));
        }
        return (bindings);
    }


    private static Object saveList(List<String> list) {
        if ((list == null) || list.isEmpty()) {
            return null;
        }

        int size = list.size();

        if (size == 1) {
            return list.get(0);
        }

        return list.toArray(new String[size]);
    }

    private static List<String> restoreList(String propertyName, 
                                            Object state) {

        if (state == null) {
            return null;
        }

        List<String> list = null;

        if (state instanceof String) {
            list = toSingletonList(propertyName, (String)state);
        } else if (state instanceof String[]) {
            list = Collections.unmodifiableList(Arrays.asList((String[])state));
        }

        return list;
    }
      
    private <T> Object eval(String propertyName, Object value, T defaultValue) {

        if (value != null) {
            return value;
        }

        ValueExpression expression = getValueExpression(propertyName);

        if (expression != null) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            return expression.getValue(ctx.getELContext());
        }

        return defaultValue;
    }


    @SuppressWarnings("unchecked")
	private Collection<String> getCollectionValue(String propertyName,
                                                  Collection<String> collection) {
        if (collection!= null) {
            return collection;
        }

        Collection<String> result = null;
        ValueExpression expression = getValueExpression(propertyName);

        if (expression != null) {

            FacesContext ctx = FacesContext.getCurrentInstance();
            Object value = expression.getValue(ctx.getELContext());

            if (value != null) {

                if (value instanceof Collection) {
                    // Unchecked cast to Collection<String>
                    return (Collection<String>)value;
                }

                result = toList(propertyName, expression, value);
            }
        }

        return result == null ? Collections.<String>emptyList() : result;
    }

    private void setLiteralValue(String propertyName,
                                 ValueExpression expression) {

        assert(expression.isLiteralText());

        Object value;
        ELContext context = FacesContext.getCurrentInstance().getELContext();

        try {
            value = expression.getValue(context);
        } catch (ELException ele) {
            throw new FacesException(ele);
        }

        if (ASYNC.equals(propertyName)) {
            async = (Boolean)value;
        } else if (GLOBAL.equals(propertyName)) {
            global = (Boolean)value;
        } else if (ONCOMPLETE.equals(propertyName)) {
            oncomplete = (String)value;
        } else if (ONSTART.equals(propertyName)) {
            onstart = (String)value;
        } else if (ONSUCCESS.equals(propertyName)) {
            onsuccess = (String)value;
        } else if (ONERROR.equals(propertyName)) {
            onerror = (String)value;
        } else if (IMMEDIATE.equals(propertyName)) {
            immediate = (Boolean)value;
        } else if (DISABLED.equals(propertyName)) {
            disabled = (Boolean)value;
        } else if (PROCESS.equals(propertyName)) {
            process = toList(propertyName, expression, value);
        } else if (UPDATE.equals(propertyName)) {
            update = toList(propertyName, expression, value);
        }
    }

    private static List<String> toList(String propertyName,
                                ValueExpression expression,
                                Object value) {

        if (value instanceof String) {

            String strValue = (String)value;

            // If the value contains no spaces, we can optimize.
            // This is worthwhile, since the execute/render lists
            // will often only contain a single value.
            if (strValue.indexOf(' ') == -1 && strValue.indexOf(',') == -1) {
                return toSingletonList(propertyName, strValue);
            }

            // We're stuck splitting up the string.
            String[] values = SPLIT_PATTERN.split(strValue);
            if ((values == null) || (values.length == 0)) {
                return null;
            }

            // Note that we could create a Set out of the values if
            // we care about removing duplicates.  However, the
            // presence of duplicates does not real harm.  They will
            // be consolidated during the partial view traversal.  So,
            // just create an list - garbage in, garbage out.
            return Collections.unmodifiableList(Arrays.asList(values));
        }

        throw new FacesException(expression.toString()
                                 + " : '"
                                 + propertyName
                                 + "' attribute value must be either a String or a Collection");
    }

    private static List<String> toSingletonList(String propertyName,
                                         String value) {
        if ((null == value) || (value.length() == 0)) {
            return null;
        }

        if (value.charAt(0) == '@') {
            // These are very common, so we use shared copies
            // of these collections instead of re-creating.
            List<String> list;

            if (ALL.equals(value)) {
                list = ALL_LIST;
            } else if (FORM.equals(value)){
                list = FORM_LIST;
            } else if (THIS.equals(value)) {
                list = THIS_LIST; 
            } else if (NONE.equals(value)) {
                list = NONE_LIST;
            } else {
                throw new FacesException(value
                                     + " : Invalid id keyword specified for '"
                                     + propertyName
                                     + "' attribute");
            }
            
            return list;
        }
         
        return Collections.singletonList(value);
    }

    // Makes a defensive copy of the collection, converting to a List
    // (to make state saving a bit easier).
    private List<String> copyToList(Collection<String> collection) {
 
        if ((collection == null) || collection.isEmpty()) {
            return null;
        }

       return Collections.unmodifiableList(new ArrayList <String>(collection));
    }

    // Property name constants
    private static final String ASYNC = "async";
    private static final String GLOBAL = "global";
    private static final String ONCOMPLETE = "oncomplete";
    private static final String ONSTART = "onstart";
    private static final String ONSUCCESS = "onsuccess";
    private static final String ONERROR = "onerror";
    private static final String IMMEDIATE = "immediate";
    private static final String DISABLED = "disabled";
    private static final String PROCESS = "process";
    private static final String UPDATE = "update";

    // Id keyword constants
    private static String ALL = "@all";
    private static String FORM = "@form";
    private static String THIS = "@this";
    private static String NONE = "@none";

    // Shared execute/render collections
    private static List<String> ALL_LIST = Collections.singletonList("@all");
    private static List<String> FORM_LIST = Collections.singletonList("@form");
    private static List<String> THIS_LIST = Collections.singletonList("@this");
    private static List<String> NONE_LIST = Collections.singletonList("@none");

    // Pattern used for process/update string splitting
    private static Pattern SPLIT_PATTERN = Pattern.compile("\\s+|(\\s*,\\s*)");
}
