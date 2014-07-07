/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.link;

import java.util.List;

import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.documentElements.DocumentElements;
import com.gistlabs.mechanize.document.node.Node;

/** 
 * A collection of Link objects. 
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class Links extends DocumentElements<Link> {
	public Links(Resource page) {
		this(page, null);
	}
	
	public Links(Resource page, List<? extends Node> links) {
		super(page, links);
	}
	
	@Override
	protected Link newRepresentation(Node node) {
		return new Link(getPage(), node);
	}
}
