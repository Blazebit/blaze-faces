package com.blazebit.blazefaces.examples.view;

import java.io.InputStream;

import com.blazebit.blazefaces.model.DefaultStreamedContent;
import com.blazebit.blazefaces.model.StreamedContent;

public class MediaController {
	
	private StreamedContent media;
	
	public MediaController() {
		InputStream stream = this.getClass().getResourceAsStream("ria_with_blazefaces.mov");
		media = new DefaultStreamedContent(stream, "video/quicktime");
	}

	public StreamedContent getMedia() {
		return media;
	}

	public void setMedia(StreamedContent media) {
		this.media = media;
	}

}