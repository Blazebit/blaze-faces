package com.blazebit.blazefaces.apt.model;

public class Renderer {

	private String type;
	private String clazz;
	private String componentFamily;
	
	public Renderer() {
	}

	public Renderer(String type, String clazz, String componentFamily) {
		this.type = type;
		this.clazz = clazz;
		this.componentFamily = componentFamily;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getComponentFamily() {
		return componentFamily;
	}

	public void setComponentFamily(String componentFamily) {
		this.componentFamily = componentFamily;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		result = prime * result
				+ ((componentFamily == null) ? 0 : componentFamily.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if (!(obj instanceof Renderer)) {
			return false;
		}
		Renderer other = (Renderer) obj;
		if (clazz == null) {
			if (other.clazz != null) {
				return false;
			}
		} else if (!clazz.equals(other.clazz)) {
			return false;
		}
		if (componentFamily == null) {
			if (other.componentFamily != null) {
				return false;
			}
		} else if (!componentFamily.equals(other.componentFamily)) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		return true;
	}
}
