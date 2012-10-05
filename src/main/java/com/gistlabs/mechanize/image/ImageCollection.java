package com.gistlabs.mechanize.image;

import java.util.HashSet;
import java.util.Set;

/**
 * Collection of image names that has been already successfully requested.
 * @author Martin.Kersten <Martin.Kersten.mk@gmail.com>
 */
public class ImageCollection {
	private Set<String> imageUrl = new HashSet<String>();
	
	public boolean hasLoaded(Image image) {
		return imageUrl.contains(image.getAbsoluteSrc());
	}
	
	public void markAsLoaded(Image image) {
		imageUrl.add(image.getAbsoluteSrc());
	}
	
	public int size() {
		return imageUrl.size();
	}
}
