package com.blazebit.blazefaces.examples.view;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class ImageSwitchBean {

	private List<String> images;

	public ImageSwitchBean() {
		images = new ArrayList<String>();
		images.add("nature1.jpg");
		images.add("nature2.jpg");
		images.add("nature3.jpg");
		images.add("nature4.jpg");
	}

	public List<String> getImages() {
		return images;
	}
}
