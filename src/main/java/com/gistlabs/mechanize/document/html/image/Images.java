/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html.image;

import java.io.OutputStream;
import java.util.List;

import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.documentElements.DocumentElements;
import com.gistlabs.mechanize.document.node.Node;
import com.gistlabs.mechanize.document.query.QueryStrategy;
import com.gistlabs.mechanize.util.NullOutputStream;
import com.gistlabs.mechanize.util.Util;

/**
 * A collection of Image objects.
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class Images extends DocumentElements<Image> {

	public Images(final Resource page, final QueryStrategy queryStrategy) {
		this(page, newEmptyList(), queryStrategy);
	}

	private static List<? extends Node>newEmptyList() {
		return Util.newEmptyList();
	}

	public Images (final Resource page, final List<? extends Node> forms, final QueryStrategy queryStrategy) {
		super(page, forms);
	}

	@Override
	protected Image newRepresentation(final Node node) {
		return new Image(page, node);
	}

	public ImageCollection loadAll() {
		return loadAllMissing(new ImageCollection());
	}

	/** Returns the given image collection after loading all missing images that are present within the
	 *  page but not marked as being already loaded within the image collection. */
	public ImageCollection loadAllMissing(final ImageCollection imageCollection) {
		for(Image image : this)
			if(!imageCollection.hasLoaded(image)) {
				OutputStream out = new NullOutputStream();
				getPage().getAgent().get(image.getAbsoluteSrc()).saveTo(out);
				imageCollection.markAsLoaded(image);
			}
		return imageCollection;
	}
}