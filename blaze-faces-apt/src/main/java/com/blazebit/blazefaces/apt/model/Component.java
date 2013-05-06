package com.blazebit.blazefaces.apt.model;

import java.util.ArrayList;
import java.util.List;

public class Component implements TagHolder{
	private String family;
	private String type;
	private String renderer;
	private String rendererType;
	private String handler;
	private String clazz;
	private String parent;
	private boolean isAbstract;
        private List<String> imports = new ArrayList<String>(0);
	private Tag tag = new Tag(this);
	
	public Component() {
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

	public String getRendererType() {
		return rendererType;
	}

	public void setRendererType(String rendererType) {
		this.rendererType = rendererType;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	
	public String getShortName() {
		return clazz.substring(clazz.lastIndexOf('.') + 1);
	}
	
        @Override
	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

        @Override
	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

        @Override
	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public String getPackage() {
		return clazz == null ? null : clazz.substring(0, clazz.lastIndexOf('.'));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
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
		if (!(obj instanceof Component)) {
			return false;
		}
		Component other = (Component) obj;
		if (clazz == null) {
			if (other.clazz != null) {
				return false;
			}
		} else if (!clazz.equals(other.clazz)) {
			return false;
		}
		return true;
	}
	
}
