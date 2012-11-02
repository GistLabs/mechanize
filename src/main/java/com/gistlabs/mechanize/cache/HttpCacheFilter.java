/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.cache;

import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

import com.gistlabs.mechanize.filters.MechanizeChainFilter;
import com.gistlabs.mechanize.filters.MechanizeFilter;
import com.gistlabs.mechanize.util.Collections;

/**
 * Support for cache and conditional HTTP request/response handling
 * 
 * Includes support for the following HTTP Headers to support caching: Cache-Control, Expires, Date, Age
 * Includes support for the following HTTP Headers to support conditions: Last-Modified, E-Tag, If-Modified-Since, If-None-Match
 *
 */
public class HttpCacheFilter implements MechanizeChainFilter {
	final static Collection<String> CACHE_METHODS = Collections.collection("GET"); // TODO Add HEAD to this list and cache results

	final HttpCache cache;

	public HttpCacheFilter() {
		this(new InMemoryHttpCache());
	}

	public HttpCacheFilter(final HttpCache cache) {
		this.cache = cache;
	}

	@Override
	public HttpResponse execute(final HttpUriRequest request, final HttpContext context, final MechanizeFilter chain) {
		String uri = request.getURI().toString();

		// only handle GET requests
		if (! CACHE_METHODS.contains(request.getMethod().toUpperCase())) {
			invalidate(uri, request);
			return chain.execute(request, context);
		}

		CacheEntry previous = cache.get(uri);
		if (previous!=null && previous.isValid())
			return previous.response;

		if (previous!=null)
			previous.prepareConditionalGet(request);

		HttpResponse response = chain.execute(request, context); // call the chain

		if (response.getStatusLine().getStatusCode()==304) // not modified
			return previous.updateCacheValues(response).response;

		CacheEntry maybe = new CacheEntry(request, response);

		if (maybe.isCacheable())
			store(uri, previous, maybe);

		return response;
	}

	protected void invalidate(final String uri, final HttpUriRequest request) {
		cache.remove(uri);
	}

	protected boolean store(final String uri, final CacheEntry cachedValue, final CacheEntry maybe) {
		if (cachedValue==null)
			return cache.putIfAbsent(uri, maybe);
		else
			return cache.replace(uri, cachedValue, maybe);
	}

}
