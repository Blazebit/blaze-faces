package com.blazebit.blazefaces.apt.model;

public class Factory {

	private String partialViewContextFactory;

	public Factory() {
	}

	public Factory(String partialViewContextFactory) {
		this.partialViewContextFactory = partialViewContextFactory;
	}

	public String getPartialViewContextFactory() {
		return partialViewContextFactory;
	}

	public void setPartialViewContextFactory(String partialViewContextFactory) {
		this.partialViewContextFactory = partialViewContextFactory;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((partialViewContextFactory == null) ? 0
						: partialViewContextFactory.hashCode());
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
		if (!(obj instanceof Factory)) {
			return false;
		}
		Factory other = (Factory) obj;
		if (partialViewContextFactory == null) {
			if (other.partialViewContextFactory != null) {
				return false;
			}
		} else if (!partialViewContextFactory
				.equals(other.partialViewContextFactory)) {
			return false;
		}
		return true;
	}
}
