/*
 * Copyright 2011-2012 Blazebit
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
	@ResourceDependency(library="blazefaces", name="jquery/jquery.js"),
	@ResourceDependency(library="blazefaces", name="blazefaces.js")
})
//<attribute>
//<description>The code to be executed.</description>
//<name>code</name>
//<required>false</required>
//<type>java.lang.String</type>
//</attribute>
//<attribute>
//<description>When set to true, ajax requests are not queued.</description>
//<name>async</name>
//<required>false</required>
//<type>java.lang.Boolean</type>
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
