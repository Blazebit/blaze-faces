package com.blazebit.blazefaces.apt.model;

import java.util.ArrayList;
import java.util.List;

public class Application {

	private String resourceHandler;
	private List<SystemEventListener> systemEventListeners = new ArrayList<SystemEventListener>(0);
	
	public Application() {
	}

	public Application(String resourceHandler) {
		this.resourceHandler = resourceHandler;
	}

	public String getResourceHandler() {
		return resourceHandler;
	}

	public void setResourceHandler(String resourceHandler) {
		this.resourceHandler = resourceHandler;
	}

	public List<SystemEventListener> getSystemEventListeners() {
		return systemEventListeners;
	}

	public void setSystemEventListeners(List<SystemEventListener> systemEventListeners) {
		this.systemEventListeners = systemEventListeners;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((resourceHandler == null) ? 0 : resourceHandler.hashCode());
		result = prime
				* result
				+ ((systemEventListeners == null) ? 0 : systemEventListeners
						.hashCode());
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
		if (!(obj instanceof Application)) {
			return false;
		}
		Application other = (Application) obj;
		if (resourceHandler == null) {
			if (other.resourceHandler != null) {
				return false;
			}
		} else if (!resourceHandler.equals(other.resourceHandler)) {
			return false;
		}
		if (systemEventListeners == null) {
			if (other.systemEventListeners != null) {
				return false;
			}
		} else if (!systemEventListeners.equals(other.systemEventListeners)) {
			return false;
		}
		return true;
	}
}
