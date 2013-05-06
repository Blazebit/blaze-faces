package com.blazebit.blazefaces.apt.model;

import java.util.ArrayList;
import java.util.List;

public class Tag {
	private String name;
	private Description description;
	private String handler;
	private Behavior behavior;
	private Component component;
	private List<Attribute> attributes = new ArrayList<Attribute>(0);

	public Tag(Component component) {
		this.component = component;
	}
	public Tag(Behavior behavior) {
		this.behavior = behavior;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Description getDescription() {
		return description;
	}

	public void setDescription(Description description) {
		this.description = description;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public Behavior getBehavior() {
		return behavior;
	}

	public void setBehavior(Behavior behavior) {
		this.behavior = behavior;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
        
        public List<Attribute> getEffectiveAttributes() {
            List<Attribute> effectiveAttributes = new ArrayList<Attribute>(attributes.size());
            
            for(int i = 0; i < attributes.size(); i++) {
                if(!attributes.get(i).isIgnore()) {
                    effectiveAttributes.add(attributes.get(i));
                }
            }
            
            return effectiveAttributes;
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
		if (!(obj instanceof Tag)) {
			return false;
		}
		Tag other = (Tag) obj;
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
