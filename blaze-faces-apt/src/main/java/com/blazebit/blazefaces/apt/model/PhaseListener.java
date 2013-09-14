package com.blazebit.blazefaces.apt.model;

public class PhaseListener {
	private String phaseListenerClass;
	
	public PhaseListener() {
	}

    public PhaseListener(String phaseListenerClass) {
        this.phaseListenerClass = phaseListenerClass;
    }

    
    public String getPhaseListenerClass() {
        return phaseListenerClass;
    }

    
    public void setPhaseListenerClass(String phaseListenerClass) {
        this.phaseListenerClass = phaseListenerClass;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((phaseListenerClass == null) ? 0 : phaseListenerClass.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PhaseListener)) {
            return false;
        }
        PhaseListener other = (PhaseListener) obj;
        if (phaseListenerClass == null) {
            if (other.phaseListenerClass != null) {
                return false;
            }
        } else if (!phaseListenerClass.equals(other.phaseListenerClass)) {
            return false;
        }
        return true;
    }
}
