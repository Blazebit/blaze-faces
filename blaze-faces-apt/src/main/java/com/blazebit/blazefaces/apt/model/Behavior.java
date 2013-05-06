package com.blazebit.blazefaces.apt.model;

import java.util.ArrayList;
import java.util.List;

public class Behavior implements TagHolder {
	private String id;
	private String handler;
	private String clazz;
	private String parent;
        private String renderer;
	private boolean isAbstract;
        private List<String> hints = new ArrayList<String>(0);
        private List<String> imports = new ArrayList<String>(0);
	private Tag tag = new Tag(this);
        
    
	
	public Behavior() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

        @Override
	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

    public List<String> getHints() {
        return hints;
    }

    public void setHints(List<String> hints) {
        this.hints = hints;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (!(obj instanceof Behavior)) {
			return false;
		}
		Behavior other = (Behavior) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
}
