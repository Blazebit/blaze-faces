/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.context;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class RequestContext {

    private static ThreadLocal<RequestContext> instance = new ThreadLocal<RequestContext>() {

        @Override
        protected RequestContext initialValue() {
            return null;
        }
    };

    public static RequestContext getCurrentInstance() {
        return instance.get();
    }

    protected static void setCurrentInstance(RequestContext requestContext) {
        if (requestContext == null) {
            instance.remove();
        } else {
            instance.set(requestContext);
        }
    }

    @Deprecated
    public abstract boolean isAjaxRequest();

    public abstract void release();

    public abstract void addCallbackParam(String name, Object value);

    public abstract Map<String, Object> getCallbackParams();

    public abstract void addPartialUpdateTarget(String name);

    public abstract void addPartialUpdateTargets(Collection<String> collection);

    public abstract List<String> getPartialUpdateTargets();
}
