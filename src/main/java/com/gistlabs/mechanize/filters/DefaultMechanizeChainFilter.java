/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.filters;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

/**
 * Inprired by Servlet Filters, this represents executing a request and getting a repsonse.
 * 
 * Each filter in turn is called, and eventually a terminal MechanizeFilter will be called last.
 * 
 */
public class DefaultMechanizeChainFilter implements MechanizeFilter {
	final MechanizeFilter theEnd;
	final List<MechanizeChainFilter> filters = new LinkedList<MechanizeChainFilter>();

	public DefaultMechanizeChainFilter(final MechanizeFilter theEnd) {
		if (theEnd==null)
			throw new NullPointerException("The end of the processing chain can't be null!");
		this.theEnd = theEnd;
	}

	public DefaultMechanizeChainFilter add(final MechanizeChainFilter filter) {
		filters.add(filter);
		return this;
	}

	@Override
	public HttpResponse execute(final HttpUriRequest request, final HttpContext context) {
		return theEnd.execute(request, context);
	}
}
