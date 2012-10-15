/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.link;

import java.util.List;

import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.Node;
import com.gistlabs.mechanize.document.PageElements;

/** 
 * A collection of Link objects. 
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class Links extends PageElements<Link> {
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
