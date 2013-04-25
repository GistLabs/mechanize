/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.filters;

import java.util.LinkedList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

/**
 * Inprired by Servlet Filters, this represents executing a request and getting a response.
 *
 * Each filter in turn is called, and eventually a terminal MechanizeFilter will be called last.
 *
 */
public class DefaultMechanizeChainFilter implements MechanizeFilter {
	final MechanizeFilter theEnd;
	final LinkedList<MechanizeChainFilter> filters = new LinkedList<MechanizeChainFilter>();

	public DefaultMechanizeChainFilter(final MechanizeFilter theEnd) {
		if (theEnd==null)
			throw new NullPointerException("The end of the processing chain can't be null!");
		this.theEnd = theEnd;
	}

	/**
	 * Adds a filter, runs after those added before it
	 *
	 * @param filter
	 * @return
	 */
	public DefaultMechanizeChainFilter add(final MechanizeChainFilter filter) {
		filters.add(filter);
		return this;
	}

	/**
	 * Adds a filter, to the front of the pipeline
	 *
	 * @param filter
	 */
	public void prefix(final MechanizeChainFilter filter) {
		filters.addFirst(filter);
	}

	@Override
	public HttpResponse execute(final HttpUriRequest request, final HttpContext context) {
		return new ExecutionState().execute(request, context);
	}

	class ExecutionState implements MechanizeFilter {
		int index=0;

		@Override
		public HttpResponse execute(final HttpUriRequest request, final HttpContext context) {
			if (moreFilters())
				return filters.get(index++).execute(request, context, this);
			else
				return theEnd.execute(request, context);
		}

		public boolean moreFilters() {
			return index<filters.size();
		}
	}
}
