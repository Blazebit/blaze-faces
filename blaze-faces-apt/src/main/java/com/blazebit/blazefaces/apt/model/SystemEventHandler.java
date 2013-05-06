package com.blazebit.blazefaces.apt.model;

public class SystemEventHandler {
	private String sourceClass;
	private String systemEventClass;
	private String systemEventListenerClass;
	
	public SystemEventHandler() {
	}

	public SystemEventHandler(String sourceClass, String systemEventClass,
			String systemEventListenerClass) {
		this.sourceClass = sourceClass;
		this.systemEventClass = systemEventClass;
		this.systemEventListenerClass = systemEventListenerClass;
	}

	public String getSourceClass() {
		return sourceClass;
	}

	public void setSourceClass(String sourceClass) {
		this.sourceClass = sourceClass;
	}

	public String getSystemEventClass() {
		return systemEventClass;
	}

	public void setSystemEventClass(String systemEventClass) {
		this.systemEventClass = systemEventClass;
	}

	public String getSystemEventListenerClass() {
		return systemEventListenerClass;
	}

	public void setSystemEventListenerClass(String systemEventListenerClass) {
		this.systemEventListenerClass = systemEventListenerClass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sourceClass == null) ? 0 : sourceClass.hashCode());
		result = prime
				* result
				+ ((systemEventClass == null) ? 0 : systemEventClass.hashCode());
		result = prime
				* result
				+ ((systemEventListenerClass == null) ? 0
						: systemEventListenerClass.hashCode());
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
		if (!(obj instanceof SystemEventHandler)) {
			return false;
		}
		SystemEventHandler other = (SystemEventHandler) obj;
		if (sourceClass == null) {
			if (other.sourceClass != null) {
				return false;
			}
		} else if (!sourceClass.equals(other.sourceClass)) {
			return false;
		}
		if (systemEventClass == null) {
			if (other.systemEventClass != null) {
				return false;
			}
		} else if (!systemEventClass.equals(other.systemEventClass)) {
			return false;
		}
		if (systemEventListenerClass == null) {
			if (other.systemEventListenerClass != null) {
				return false;
			}
		} else if (!systemEventListenerClass
				.equals(other.systemEventListenerClass)) {
			return false;
		}
		return true;
	}
}
