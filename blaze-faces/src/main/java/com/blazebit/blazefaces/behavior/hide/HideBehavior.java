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
//<attribute>
//<description>The id of the component that should be affected of this behavior.</description>
//<name>forId</name>
//<required>true</required>
//<type>java.lang.String</type>
//</attribute>
//<attribute>
//<description>Disables ajax behavior.</description>
//<name>disabled</name>
//<required>false</required>
//<type>java.lang.Boolean</type>
//</attribute>
//<attribute>
//<description>Client side event to trigger ajax request. Default value is defined by parent ClientBehaviorHolder component the behavior is attached to.</description>
//<name>event</name>
//<required>false</required>
//<type>java.lang.String</type>
//</attribute>
public class HideBehavior extends EffectBehavior {

    public static final String BEHAVIOR_ID = "com.blazebit.blazefaces.behavior.HideBehavior";
    
    @Override
    public String getRendererType() {
        return BEHAVIOR_ID;
    }
}
