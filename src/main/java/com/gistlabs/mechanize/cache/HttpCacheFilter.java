/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.cache;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

import com.gistlabs.mechanize.filters.MechanizeChainFilter;
import com.gistlabs.mechanize.filters.MechanizeFilter;

/**
 * Support for cache and conditional HTTP request/response handling
 * 
 * Includes support for the following HTTP Headers to support caching: Cache-Control, Expires, Date, Age
 * Includes support for the following HTTP Headers to support conditions: Last-Modified, ETag, If-Modified-Since, If-None-Match
 *
 */
public class HttpCacheFilter implements MechanizeChainFilter {
	final HttpCache cache;

	public HttpCacheFilter() {
		this(new InMemoryHttpCache());
	}

	public HttpCacheFilter(final HttpCache cache) {
		this.cache = cache;
	}

	@Override
	public HttpResponse execute(final HttpUriRequest request, final HttpContext context, final MechanizeFilter chain) {
		String method = request.getMethod().toUpperCase();
		if (method.equals("GET"))
			return executeGET(request, context, chain);
		else if (method.equals("HEAD"))
			return executeHEAD(request, context, chain);
		else
			return executeOther(request, context, chain);
	}

	public HttpResponse executeOther(final HttpUriRequest request, final HttpContext context, final MechanizeFilter chain) {
		String uri = request.getURI().toString();
		invalidate(uri, request);
		return chain.execute(request, context);
	}

	public HttpResponse executeGET(final HttpUriRequest request, final HttpContext context, final MechanizeFilter chain) {
		String uri = request.getURI().toString();
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

	public HttpResponse executeHEAD(final HttpUriRequest request, final HttpContext context, final MechanizeFilter chain) {
		String uri = request.getURI().toString();
		CacheEntry previous = cache.get(uri);

		if (previous!=null && previous.isValid())
			return previous.head();

		if (previous!=null)
			previous.prepareConditionalGet(request);

		HttpResponse response = chain.execute(request, context); // call the chain

		if (previous!=null)
			previous.updateCacheValues(response);

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
