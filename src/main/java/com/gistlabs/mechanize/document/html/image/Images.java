/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html.image;

import java.util.List;

import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.documentElements.DocumentElements;
import com.gistlabs.mechanize.document.node.Node;
import com.gistlabs.mechanize.util.Util;

/**
 * A collection of Image objects.
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class Images extends DocumentElements<Image> {

	public Images(final Resource page) {
		this(page, newEmptyList());
	}

	private static List<? extends Node>newEmptyList() {
		return Util.newEmptyList();
	}

	public Images (final Resource page, final List<? extends Node> forms) {
		super(page, forms);
	}

	@Override
	protected Image newRepresentation(final Node node) {
		return new Image(page, node);
	}
}