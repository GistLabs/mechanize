/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.cache.api;

public interface HttpCache {

	CacheEntry get(String uri);

	void remove(String uri);

	boolean putIfAbsent(String uri, CacheEntry maybe);

	boolean replace(String uri, CacheEntry cachedValue, CacheEntry maybe);

}
