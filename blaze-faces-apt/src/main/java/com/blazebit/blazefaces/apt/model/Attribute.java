package com.blazebit.blazefaces.apt.model;

public class Attribute {

	private String name;
	private Description description;
	private boolean required = false;
	private String type;
	private String objectType;
	private String defaultValue = "null";
	private boolean ignore = false;
	
	public Attribute() {
	}

	public Attribute(String name, Description description, boolean required,
			String type, String objectType, String defaultValue) {
		this.name = name;
		this.description = description;
		this.required = required;
		this.type = type;
		this.objectType = objectType;
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getCapitalizedName() {
		return new StringBuilder(name.length()).append(Character.toUpperCase(name.charAt(0))).append(name, 1, name.length()).toString();
	}

	public Description getDescription() {
		return description;
	}

	public void setDescription(Description description) {
		this.description = description;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isIgnore() {
		return ignore;
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (!(obj instanceof Attribute)) {
			return false;
		}
		Attribute other = (Attribute) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
}
