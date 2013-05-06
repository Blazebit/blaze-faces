package com.blazebit.blazefaces.apt.model;

public class Icon {

	private String smallIcon;
	private String largeIcon;
	
	public Icon() {
	}
	
	public Icon(String smallIcon, String largeIcon) {
		this.smallIcon = smallIcon;
		this.largeIcon = largeIcon;
	}
	public String getSmallIcon() {
		return smallIcon;
	}
	public void setSmallIcon(String smallIcon) {
		this.smallIcon = smallIcon;
	}
	public String getLargeIcon() {
		return largeIcon;
	}
	public void setLargeIcon(String largeIcon) {
		this.largeIcon = largeIcon;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((largeIcon == null) ? 0 : largeIcon.hashCode());
		result = prime * result
				+ ((smallIcon == null) ? 0 : smallIcon.hashCode());
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
		if (!(obj instanceof Icon)) {
			return false;
		}
		Icon other = (Icon) obj;
		if (largeIcon == null) {
			if (other.largeIcon != null) {
				return false;
			}
		} else if (!largeIcon.equals(other.largeIcon)) {
			return false;
		}
		if (smallIcon == null) {
			if (other.smallIcon != null) {
				return false;
			}
		} else if (!smallIcon.equals(other.smallIcon)) {
			return false;
		}
		return true;
	}
}
