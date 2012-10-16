/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.form;

import java.util.List;

import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.Node;
import com.gistlabs.mechanize.document.PageElements;
import com.gistlabs.mechanize.document.query.QueryStrategy;
import com.gistlabs.mechanize.util.Util;

/** 
 * A collection of Form objects. 
 *
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class Forms extends PageElements<Form> {
	public Forms(Resource page, QueryStrategy queryStrategy) {
		this(page, createList(), queryStrategy);
	}

	private static List<? extends Node> createList() {
		return Util.newEmptyList();
	}



	public Forms(Resource page, List<? extends Node> forms, QueryStrategy queryStrategy) {
		super(page, forms, queryStrategy);
	}
	
	@Override
	protected Form newRepresentation(Node node) {
		return new Form(getPage(), node);
	}
}
