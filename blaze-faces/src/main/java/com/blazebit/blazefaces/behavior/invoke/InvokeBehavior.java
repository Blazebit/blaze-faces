/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.behavior.invoke;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.behavior.ClientBehaviorBase;

/**
 *
 * @author Christian Beikov
 */
@ResourceDependencies({
    @ResourceDependency(library = "blazefaces", name = "jquery/jquery.js"),
    @ResourceDependency(library = "blazefaces", name = "core/core.js")})
public class InvokeBehavior extends ClientBehaviorBase {

    public static final String BEHAVIOR_ID = "com.blazebit.blazefaces.behavior.InvokeBehavior";
    private boolean async = false;
    private boolean disabled = false;
    private String code;

    @Override
    public String getRendererType() {
        return BEHAVIOR_ID;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
