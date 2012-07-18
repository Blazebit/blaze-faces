/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

public class BlazeRequestContext extends RequestContext {

    private final static String CALLBACK_PARAMS_KEY = "CALLBACK_PARAMS";
    private final static String PARTIAL_UPDATE_TARGETS_KEY = "PARTIAL_UPDATE_TARGETS_KEY";
    private Map<Object, Object> attributes;

    public BlazeRequestContext() {
        this.attributes = new HashMap<Object, Object>();
    }
    
    public static void init(){
        setCurrentInstance(new BlazeRequestContext());
    }

    @Override
    public boolean isAjaxRequest() {
        return FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest();
    }

    @Override
    public void release() {
        attributes = null;

        setCurrentInstance(null);
    }

    @Override
    public void addCallbackParam(String name, Object value) {
        getCallbackParams().put(name, value);
    }

    @Override
    public void addPartialUpdateTarget(String target) {
        getPartialUpdateTargets().add(target);
    }

    @Override
    public void addPartialUpdateTargets(Collection<String> collection) {
       getPartialUpdateTargets().addAll(collection);
    }

    @SuppressWarnings("unchecked")
	@Override
    public Map<String, Object> getCallbackParams() {
        if (attributes.get(CALLBACK_PARAMS_KEY) == null) {
            attributes.put(CALLBACK_PARAMS_KEY, new HashMap<String, Object>());
        }
        return (Map<String, Object>) attributes.get(CALLBACK_PARAMS_KEY);
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<String> getPartialUpdateTargets() {
        if (attributes.get(PARTIAL_UPDATE_TARGETS_KEY) == null) {
            attributes.put(PARTIAL_UPDATE_TARGETS_KEY, new ArrayList<String>());
        }
        return (List<String>) attributes.get(PARTIAL_UPDATE_TARGETS_KEY);
    }
}