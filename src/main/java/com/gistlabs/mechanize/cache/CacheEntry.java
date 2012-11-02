/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.cache;

import java.util.Calendar;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

import com.gistlabs.mechanize.exceptions.MechanizeExceptionFactory;

class CacheEntry {
	private static final Date OLD = new Date(0);
	final HttpUriRequest request;
	final HttpResponse response;

	/**
	 * Used when the response doesn't include a Date header...
	 */
	final Date date = new Date();

	public CacheEntry(final HttpUriRequest request, final HttpResponse response) {
		this.request = request;
		this.response = response;

	}

	/**
	 * Is this a cacheable response? Does it indicate either caching or conditional checks?
	 */
	public boolean isCacheable() {
		boolean mustNotCache =
				has("Pragma", "no-cache", this.response)
				|| has("Cache-Control", "no-cache", this.response);

		if (mustNotCache)
			return false;

		boolean supportsConditionals = has("Last-Modified", this.response) || has("E-Tag", this.response);
		boolean isCacheable = has("Cache-Control", this.response)|| has("Expires", this.response);
		return supportsConditionals || isCacheable;
	}

	/**
	 * Is this cache entry still within valid time checks? (Don't need to call the server...)
	 * 
	 * @return
	 */
	public boolean isValid() {
		Date now = new Date();
		Date cacheControl = getCacheControlExpiresDate();
		Date expires = getExpiresDate();

		return now.before(cacheControl) || now.before(expires);
	}

	/**
	 * The size of the entity content
	 * 
	 * @return
	 */
	public long byteCount() {
		return this.response.getEntity().getContentLength();
	}

	/**
	 * 
	 * @return
	 */
	private Date getDate() {
		String expires = get("Date", this.response);

		if (expires.equals(""))
			return date; // will this ever happen?
		else
			return parseDate(expires);
	}

	private Date getCacheControlExpiresDate() {
		String maxage = get("Cache-Control", "max-age", this.response);
		if (maxage.equals(""))
			return OLD;

		int seconds = Integer.parseInt(maxage);
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDate());
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}

	private Date getExpiresDate() {
		String expires = get("Expires", this.response);

		try {
			return DateUtils.parseDate(expires);
		} catch(Exception e) { // if not present or parsable... just ignore
			return OLD;
		}
	}

	protected Date parseDate(final String expires) {
		try {
			return DateUtils.parseDate(expires);
		} catch (DateParseException e) {
			throw MechanizeExceptionFactory.newException(e);
		}
	}

	public CacheEntry updateCacheValues(final HttpResponse response) {
		transferFirstHeader("Date", response, this.response);
		transferFirstHeader("E-Tag", response, this.response);
		transferFirstHeader("Last-Modified", response, this.response);
		transferFirstHeader("Cache-Control", response, this.response);
		transferFirstHeader("Expires", response, this.response);
		return this;
	}

	public void prepareConditionalGet(final HttpUriRequest newRequest) {
		transferFirstHeader("E-Tag", "If-None-Match", this.response, newRequest);
		transferFirstHeader("Last-Modified", "If-None-Match", this.response, newRequest);
	}

	protected void transferFirstHeader(final String headerName, final HttpMessage origin, final HttpMessage dest) {
		transferFirstHeader(headerName, headerName, origin, dest);
	}

	protected void transferFirstHeader(final String headerName, final String destName, final HttpMessage origin, final HttpMessage dest) {
		Header header = origin.getFirstHeader(headerName);
		if (header!=null)
			dest.setHeader(destName, header.getValue());
	}

	protected boolean has(final String headerName, final HttpMessage message) {
		return message.getHeaders(headerName).length>0;
	}

	protected String get(final String headerName, final HttpMessage message) {
		Header header = message.getFirstHeader(headerName);
		if (header!=null)
			return header.getValue();
		else
			return "";
	}

	protected boolean has(final String headerName, final String headerValueOrElement, final HttpMessage message) {
		Header[] headers = message.getHeaders(headerName);
		for (Header header : headers) {
			if (header.getValue().equals(headerValueOrElement))
				return true;

			HeaderElement[] elements = header.getElements();
			for (HeaderElement element : elements)
				if (element.getName().equals(headerValueOrElement))
					return true;
		}
		return false;
	}

	protected String get(final String headerName, final String elementName, final HttpMessage message) {
		Header header = message.getFirstHeader(headerName);
		if (header==null)
			return "";

		HeaderElement[] elements = header.getElements();
		for (HeaderElement element : elements)
			if (element.getName().equals(elementName))
				return element.getValue();

		return "";
	}
}
