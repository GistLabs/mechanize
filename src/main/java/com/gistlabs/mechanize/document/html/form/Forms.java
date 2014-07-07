/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html.form;

import java.util.List;

import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.documentElements.DocumentElements;
import com.gistlabs.mechanize.document.node.Node;
import com.gistlabs.mechanize.util.Util;

/** 
 * A collection of Form objects. 
 *
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class Forms extends DocumentElements<Form> {
	public Forms(Resource page) {
		this(page, createList());
	}

	private static List<? extends Node> createList() {
		return Util.newEmptyList();
	}



	public Forms(Resource page, List<? extends Node> forms) {
		super(page, forms);
	}
	
	@Override
	protected Form newRepresentation(Node node) {
		return new Form(getPage(), node);
	}
}
