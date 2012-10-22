/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html.image;

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
