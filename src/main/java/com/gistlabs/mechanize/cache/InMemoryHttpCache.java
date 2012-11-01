package com.gistlabs.mechanize.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryHttpCache implements HttpCache {
	final ConcurrentMap<String,CacheEntry> cache = new ConcurrentHashMap<String,CacheEntry>(1024);
	final ConcurrentLinkedQueue<String> uriFifo = new ConcurrentLinkedQueue<String>();
	final long maxBytes;
	final AtomicLong currentBytes = new AtomicLong(0);

	public InMemoryHttpCache() {
		this(64);
	}

	public InMemoryHttpCache(final int numberOfMegabytes) {
		maxBytes = 1024 * 1024 * numberOfMegabytes;
	}

	@Override
	public CacheEntry get(final String uri) {
		CacheEntry entry = cache.get(uri);

		if (entry!=null) { // refresh LRU
			uriFifo.remove(uri);
			uriFifo.offer(uri);
		}

		return entry;
	}

	@Override
	public void remove(final String uri) {
		CacheEntry removed = cache.remove(uri);
		uriFifo.remove(uri);

		if (removed!=null)
			currentBytes.addAndGet(-getByteCount(removed));
	}

	@Override
	public void putIfAbsent(final String uri, final CacheEntry maybe) {
		if (!ensureCapacity(getByteCount(maybe)))
			return; // and don't cache

		CacheEntry previous = cache.putIfAbsent(uri, maybe);

		currentBytes.addAndGet(getByteCount(maybe) - getByteCount(previous));
	}

	@Override
	public void replace(final String uri, final CacheEntry cachedValue, final CacheEntry maybe) {
		if (!ensureCapacity(getByteCount(maybe))) {
			remove(uri);
			return; // and don't cache
		}

		boolean replaced = cache.replace(uri, cachedValue, maybe);

		if (replaced)
			currentBytes.addAndGet(getByteCount(maybe)-getByteCount(cachedValue));
	}

	protected boolean ensureCapacity(final long byteCount) {
		if (byteCount>maxBytes)
			return false;

		while(currentBytes.get()+byteCount > maxBytes)
			remove(uriFifo.poll());

		return true;
	}

	/**
	 * null safe get byte count helper
	 * 
	 * @param entry
	 * @return
	 */
	protected long getByteCount(final CacheEntry entry) {
		return entry==null ? 0 : entry.byteCount();
	}
}
