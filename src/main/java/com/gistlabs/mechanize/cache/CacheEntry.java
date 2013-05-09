package com.gistlabs.mechanize.cache;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

interface CacheEntry {

	/**
	 * Is this a cacheable response? Does it indicate either caching or conditional checks?
	 */
	public abstract boolean isCacheable();

	/**
	 * Is this cache entry still within valid time checks? (Don't need to call the server...)
	 *
	 * @return
	 */
	public abstract boolean isValid();

	/**
	 * The size of the entity content
	 *
	 * @return
	 */
	public abstract long byteCount();

	/**
	 * @return Get the cached response
	 */
	public abstract HttpResponse getResponse();

	public abstract CacheEntry updateCacheValues(final HttpResponse response);

	public abstract void prepareConditionalGet(final HttpUriRequest newRequest);

	/**
	 * Return a response to a HEAD request (status line, headers, no entity body)
	 * @return
	 */
	public abstract HttpResponse head();

}

