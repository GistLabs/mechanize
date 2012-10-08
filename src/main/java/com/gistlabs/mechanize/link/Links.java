/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.link;

import java.util.List;

import com.gistlabs.mechanize.Page;
import com.gistlabs.mechanize.PageElements;
import com.gistlabs.mechanize.html.HtmlElement;

/** 
 * A collection of Link objects. 
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class Links extends PageElements<Link> {
	public Links(Page page) {
		this(page, null);
	}
	
	public Links(Page page, List<HtmlElement> links) {
		super(page, links);
	}
	
	@Override
	protected Link newRepresentation(HtmlElement element) {
		return new Link(getPage(), element);
	}
}
