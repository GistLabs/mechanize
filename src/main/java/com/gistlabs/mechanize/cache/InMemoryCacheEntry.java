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
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.message.BasicHttpResponse;

import com.gistlabs.mechanize.exceptions.MechanizeExceptionFactory;

public class InMemoryCacheEntry implements CacheEntry {
	private static final Date OLD = new Date(0);
	final private HttpUriRequest request;
	final private HttpResponse response;

	/**
	 * Used when the response doesn't include a Date header...
	 */
	final Date date = new Date();

	public InMemoryCacheEntry(final HttpUriRequest request, final HttpResponse response) {
		if (request==null)
			throw new NullPointerException("request can't be null!");
		this.request = request;
		if (response==null)
			throw new NullPointerException("response can't be null!");
		this.response = response;

	}

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.cache.InMemoryCacheEntry#isCacheable()
	 */
	@Override
	public boolean isCacheable() {
		boolean supportsConditionals = has("Last-Modified", this.response) || has("ETag", this.response);
		boolean isCacheable =
				has("Cache-Control", "s-maxage", this.response)
				|| has("Cache-Control", "max-age", this.response)
				|| has("Expires", this.response);
		return supportsConditionals || isCacheable;
	}

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.cache.InMemoryCacheEntry#isValid()
	 */
	@Override
	public boolean isValid() {
		boolean mustNotCache =
				has("Pragma", "no-cache", this.response)
				|| has("Cache-Control", "no-cache", this.response);

		if (mustNotCache)
			return false;

		Date now = new Date();
		Date cacheControl = getCacheControlExpiresDate();
		Date expires = getExpiresDate();

		return now.before(cacheControl) || now.before(expires);
	}

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.cache.InMemoryCacheEntry#byteCount()
	 */
	@Override
	public long byteCount() {
		return this.response.getEntity().getContentLength();
	}

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.cache.InMemoryCacheEntry#getResponse()
	 */
	@Override
	public HttpResponse getResponse() {
		return this.response;
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

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.cache.InMemoryCacheEntry#updateCacheValues(org.apache.http.HttpResponse)
	 */
	@Override
	public CacheEntry updateCacheValues(final HttpResponse response) {
		transferFirstHeader("Date", response, this.response);
		transferFirstHeader("ETag", response, this.response);
		transferFirstHeader("Last-Modified", response, this.response);
		transferFirstHeader("Cache-Control", response, this.response);
		transferFirstHeader("Expires", response, this.response);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.cache.InMemoryCacheEntry#prepareConditionalGet(org.apache.http.client.methods.HttpUriRequest)
	 */
	@Override
	public void prepareConditionalGet(final HttpUriRequest newRequest) {
		transferFirstHeader("ETag", "If-None-Match", this.response, newRequest);
		transferFirstHeader("Last-Modified", "If-Modified-Since", this.response, newRequest);
	}

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.cache.InMemoryCacheEntry#head()
	 */
	@Override
	public HttpResponse head() {
		BasicHttpResponse response = new BasicHttpResponse(this.response.getStatusLine());
		Header[] allHeaders = this.response.getAllHeaders();
		for (Header allHeader : allHeaders)
			response.addHeader(allHeader);
		response.setEntity(new ByteArrayEntity(new byte[]{}));
		return response;
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
