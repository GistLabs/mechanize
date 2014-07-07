/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.cache.nil;

import com.gistlabs.mechanize.cache.api.CacheEntry;
import com.gistlabs.mechanize.cache.api.HttpCache;


/**
 * This implementation performs no caching at all...
 */
public class NilHttpCache implements HttpCache {

	public NilHttpCache() {
	}

	@Override
	public CacheEntry get(final String uri) {
		return null;
	}

	@Override
	public void remove(final String uri) {
	}

	@Override
	public boolean putIfAbsent(final String uri, final CacheEntry maybe) {
		return false;
	}

	@Override
	public boolean replace(final String uri, final CacheEntry cachedValue, final CacheEntry maybe) {
		return false;
	}
}
