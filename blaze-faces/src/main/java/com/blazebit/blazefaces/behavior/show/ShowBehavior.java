/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.behavior.show;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import com.blazebit.blazefaces.behavior.EffectBehavior;

/**
 *
 * @author Christian Beikov
 */
@ResourceDependencies({
    @ResourceDependency(library = "blazefaces", name = "jquery/jquery.js"),
    @ResourceDependency(library = "blazefaces", name = "core/core.js")})
public class ShowBehavior extends EffectBehavior {

    public static final String BEHAVIOR_ID = "com.blazebit.blazefaces.behavior.ShowBehavior";

    @Override
    public String getRendererType() {
        return BEHAVIOR_ID;
    }
}
