/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazebit.blazefaces.component;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

public class WrapperEvent extends FacesEvent {

    public WrapperEvent(UIComponent component, FacesEvent event, String rowKey) {
        super(component);
        this.event = event;
        this.rowKey = rowKey;
    }

    private FacesEvent event = null;
    private String rowKey = null;

    public FacesEvent getFacesEvent() {
        return (this.event);
    }

    public String getRowKey() {
        return rowKey;
    }

    public PhaseId getPhaseId() {
        return (this.event.getPhaseId());
    }

    public void setPhaseId(PhaseId phaseId) {
        this.event.setPhaseId(phaseId);
    }

    public boolean isAppropriateListener(FacesListener listener) {
        return (false);
    }

    public void processListener(FacesListener listener) {
        throw new IllegalStateException();
    }
}
