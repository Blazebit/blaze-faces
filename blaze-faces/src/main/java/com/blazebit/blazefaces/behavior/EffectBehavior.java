/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.behavior;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorHint;

/**
 *
 * @author Christian Beikov
 */
public class EffectBehavior extends ClientBehaviorBase {

    public static final String BEHAVIOR_ID = "";
    public static final Set<ClientBehaviorHint> HINTS = Collections.unmodifiableSet(EnumSet.of(ClientBehaviorHint.SUBMITTING));
    private boolean disabled = false;
    private String forId;
    
    public EffectBehavior() {
    }

    public String getForId() {
        return forId;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }
    
    @Override
    public String getRendererType() {
        return BEHAVIOR_ID;
    }

    @Override
    public Set<ClientBehaviorHint> getHints() {
        return HINTS;
    }
}
