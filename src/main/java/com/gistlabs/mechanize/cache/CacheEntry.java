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

public interface CacheEntry {

	/**
	 * Is this a cacheable response? Does it indicate either caching or conditional checks?
	 */
	public boolean isCacheable();

	/**
	 * Is this cache entry still within valid time checks? (Don't need to call the server...)
	 *
	 * @return
	 */
	public boolean isValid();

	/**
	 * The size of the entity content
	 *
	 * @return
	 */
	public long byteCount();

	/**
	 * @return Get the cached response
	 */
	public HttpResponse getResponse();

	public CacheEntry updateCacheValues(final HttpResponse response);

	public void prepareConditionalGet(final HttpUriRequest newRequest);

	/**
	 * Return a response to a HEAD request (status line, headers, no entity body)
	 * @return
	 */
	public HttpResponse head();

}

