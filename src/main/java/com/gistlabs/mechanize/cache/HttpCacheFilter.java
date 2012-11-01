package com.gistlabs.mechanize.cache;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
	ConcurrentMap<String,CacheEntry> cache = new ConcurrentHashMap<String,CacheEntry>();

	@Override
	public HttpResponse execute(final HttpUriRequest request, final HttpContext context, final MechanizeFilter chain) {
		// only handle GET requests
		if (! request.getMethod().equalsIgnoreCase("GET"))
			return chain.execute(request, context);

		String uri = request.getURI().toString();
		CacheEntry previous = cache.get(uri);
		if (previous!=null && previous.isValid())
			return previous.response;

		if (previous!=null)
			previous.addConditionalHeaders(request);

		HttpResponse response = chain.execute(request, context); // call the chain
		CacheEntry maybe = new CacheEntry(request, response);

		if (maybe.isValid())
			store(uri, previous, maybe);

		return response;
	}

	protected void store(final String uri, final CacheEntry cachedValue, final CacheEntry maybe) {
		if (cachedValue==null)
			cache.putIfAbsent(uri, maybe);
		else
			cache.replace(uri, cachedValue, maybe);
	}

	class CacheEntry {
		final HttpUriRequest request;
		final HttpResponse response;

		final Date now = new Date();

		public CacheEntry(final HttpUriRequest request, final HttpResponse response) {
			this.request = request;
			this.response = response;
		}

		public boolean isValid() {
			return false;
		}

		public void addConditionalHeaders(final HttpUriRequest newRequest) {

		}
	}
}
