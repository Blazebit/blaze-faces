/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.behavior.hide;

import com.blazebit.blazefaces.behavior.EffectBehavior;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

/**
 *
 * @author Christian Beikov
 */
@ResourceDependencies({
    @ResourceDependency(library = "blazefaces", name = "jquery/jquery.js"),
    @ResourceDependency(library = "blazefaces", name = "core/core.js")})
public class HideBehavior extends EffectBehavior {

    public static final String BEHAVIOR_ID = "com.blazebit.blazefaces.behavior.HideBehavior";
    
    @Override
    public String getRendererType() {
        return BEHAVIOR_ID;
    }
}
