package com.gistlabs.mechanize.cache;

public interface HttpCache {

	CacheEntry get(String uri);

	void remove(String uri);

	void putIfAbsent(String uri, CacheEntry maybe);

	void replace(String uri, CacheEntry cachedValue, CacheEntry maybe);

}
