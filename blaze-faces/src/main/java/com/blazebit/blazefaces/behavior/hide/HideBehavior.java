/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.behavior.hide;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import com.blazebit.blazefaces.behavior.EffectBehavior;

/**
 *
 * @author Christian Beikov
 */
@ResourceDependencies({
	@ResourceDependency(library="blazefaces", name="jquery/jquery.js"),
	@ResourceDependency(library="blazefaces", name="blazefaces.js")
})
public class HideBehavior extends EffectBehavior {

    public static final String BEHAVIOR_ID = "com.blazebit.blazefaces.behavior.HideBehavior";
    
    @Override
    public String getRendererType() {
        return BEHAVIOR_ID;
    }
}
