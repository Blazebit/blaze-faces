package com.blazebit.blazefaces.apt.model;

import java.util.ArrayList;
import java.util.List;

public class Application {

	private String resourceHandler;
	private List<SystemEventHandler> systemEventHandlers = new ArrayList<SystemEventHandler>(0);
	
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

	public List<SystemEventHandler> getSystemEventHandlers() {
		return systemEventHandlers;
	}

	public void setSystemEventHandlers(List<SystemEventHandler> systemEventHandlers) {
		this.systemEventHandlers = systemEventHandlers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((resourceHandler == null) ? 0 : resourceHandler.hashCode());
		result = prime
				* result
				+ ((systemEventHandlers == null) ? 0 : systemEventHandlers
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
		if (systemEventHandlers == null) {
			if (other.systemEventHandlers != null) {
				return false;
			}
		} else if (!systemEventHandlers.equals(other.systemEventHandlers)) {
			return false;
		}
		return true;
	}
}
