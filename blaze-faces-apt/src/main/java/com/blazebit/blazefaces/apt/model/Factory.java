package com.blazebit.blazefaces.apt.model;

public class Factory {

	private String partialViewContextFactory;
	private String exceptionHandlerFactory;

	public Factory() {
	}

	public Factory(String partialViewContextFactory, String exceptionHandlerFactory) {
		this.partialViewContextFactory = partialViewContextFactory;
		this.exceptionHandlerFactory = exceptionHandlerFactory;
	}


	public String getPartialViewContextFactory() {
		return partialViewContextFactory;
	}

	public void setPartialViewContextFactory(String partialViewContextFactory) {
		this.partialViewContextFactory = partialViewContextFactory;
	}

	public String getExceptionHandlerFactory() {
		return exceptionHandlerFactory;
	}

	public void setExceptionHandlerFactory(String exceptionHandlerFactory) {
		this.exceptionHandlerFactory = exceptionHandlerFactory;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((exceptionHandlerFactory == null) ? 0
						: exceptionHandlerFactory.hashCode());
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
		if (exceptionHandlerFactory == null) {
			if (other.exceptionHandlerFactory != null) {
				return false;
			}
		} else if (!exceptionHandlerFactory
				.equals(other.exceptionHandlerFactory)) {
			return false;
		}
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
