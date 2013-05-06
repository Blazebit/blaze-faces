package com.blazebit.blazefaces.apt.model;

import java.util.ArrayList;
import java.util.List;

public class Namespace {

	private String name;
	private String namespace;
        private String shortName;
	private List<Function> functions = new ArrayList<Function>(0);
	private List<Tag> tags = new ArrayList<Tag>(0);
	private Application application = new Application();
	private Factory factory = new Factory();
	private List<Behavior> behaviors = new ArrayList<Behavior>(0);
	private List<Component> components = new ArrayList<Component>(0);
	private List<BehaviorRenderer> clientBehaviorRenderers = new ArrayList<BehaviorRenderer>(0);
	private List<Renderer> renderers = new ArrayList<Renderer>(0);
	
	public Namespace() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

	public List<Function> getFunctions() {
		return functions;
	}

	public void setFunctions(List<Function> functions) {
		this.functions = functions;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public Factory getFactory() {
		return factory;
	}

	public void setFactory(Factory factory) {
		this.factory = factory;
	}

	public List<Behavior> getBehaviors() {
		return behaviors;
	}

	public void setBehaviors(List<Behavior> behaviors) {
		this.behaviors = behaviors;
	}

	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}

	public List<BehaviorRenderer> getClientBehaviorRenderers() {
		return clientBehaviorRenderers;
	}

	public void setClientBehaviorRenderers(
			List<BehaviorRenderer> clientBehaviorRenderers) {
		this.clientBehaviorRenderers = clientBehaviorRenderers;
	}

	public List<Renderer> getRenderers() {
		return renderers;
	}

	public void setRenderers(List<Renderer> renderers) {
		this.renderers = renderers;
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
		if (!(obj instanceof Namespace)) {
			return false;
		}
		Namespace other = (Namespace) obj;
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
