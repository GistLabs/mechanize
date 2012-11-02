/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.gistlabs.mechanize.exceptions.MechanizeException;
import com.gistlabs.mechanize.exceptions.MechanizeExceptionFactory;
import com.gistlabs.mechanize.requestor.RequestBuilder;
import com.gistlabs.mechanize.requestor.RequestBuilderFactory;
import com.gistlabs.mechanize.util.NullOutputStream;
import com.gistlabs.mechanize.util.Util;
import com.gistlabs.mechanize.util.apache.ContentType;

/**
 * Represents a resource being reseived by a request.
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public abstract class Resource implements RequestBuilderFactory<Resource> {

	@SuppressWarnings("unchecked")
	public static Collection<String> CONTENT_MATCHERS = Collections.EMPTY_LIST;

	private final MechanizeAgent agent;
	private final String uri;
	private final HttpRequestBase request;
	protected final HttpResponse response;

	/**
	 * this is just to help while debugging, it shows the request/response contents
	 */
	@SuppressWarnings("unused")
	private final Debug debugRequestResponse = new Debug(this);

	public Resource(final MechanizeAgent agent, final HttpRequestBase request, final HttpResponse response) {
		this.agent = agent;
		this.request = request;
		this.response = response;
		this.uri = inspectUri(request, response);

		try {
			loadPage();
		} catch(RuntimeException e) {
			throw e;
		} catch(Exception e) {
			throw MechanizeExceptionFactory.newException(e);
		}
	}

	protected void loadPage() throws Exception {
		preLoadContent();
	}

	protected void preLoadContent() throws IOException {
		Util.copy(getInputStream(), new NullOutputStream());
	}

	protected String getContentEncoding(final HttpResponse response) {
		try {
			ContentType contentType = ContentType.get(response.getEntity());
			return contentType.getCharset().displayName();
		} catch (NullPointerException np) {
			// TODO why don't test cases set this?
			return null;
		}
	}

	/**
	 * This will return (and cache) the response entity content input stream, or return a stream from the previously cached value.
	 * 
	 * @return
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException {
		return this.response.getEntity().getContent();
	}

	/**
	 * (From HttpEntity) Tells the length of the content, if known.
	 *
	 * @return  the number of bytes of the content, or
	 *          a negative number if unknown. If the content length is known
	 *          but exceeds {@link java.lang.Long#MAX_VALUE Long.MAX_VALUE},
	 *          a negative number is returned.
	 */
	public long getLength() {
		return this.response.getEntity().getContentLength();
	}

	protected int getIntContentLength(final HttpResponse response) {
		long longLength = response.getEntity().getContentLength();
		if (longLength<0)
			return 0;
		else if (longLength>Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		else
			return (int)longLength;
	}

	protected String inspectUri(final HttpRequestBase request, final HttpResponse response) {
		Header contentLocation = Util.findHeader(response, "content-location");
		if (contentLocation != null && contentLocation.getValue() != null)
			return contentLocation.getValue();

		Header mechanizeLocation = Util.findHeader(response, MechanizeAgent.MECHANIZE_LOCATION);
		if (mechanizeLocation!=null && mechanizeLocation.getValue()!=null)
			return mechanizeLocation.getValue();

		return request.getURI().toString();
	}

	public String getContentType() {
		return response.getEntity().getContentType().getValue();
	}

	@Override
	public RequestBuilder<Resource> doRequest(final String uri) {
		return getAgent().doRequest(uri);
	}

	/**
	 * 
	 * @return
	 */
	public String getTitle() {
		return "";
	}

	public String getUri() {
		return uri;
	}

	public long size() {
		return this.response.getEntity().getContentLength();
	}

	public HttpRequestBase getRequest() {
		return request;
	}

	public HttpResponse getResponse() {
		return response;
	}

	public MechanizeAgent getAgent() {
		return agent;
	}

	/**
	 * Serialize the contents of this page into a string
	 * 
	 * @return
	 */
	public String asString() {
		ByteArrayOutputStream result = new ByteArrayOutputStream(getIntContentLength(this.response));
		saveTo(result);
		return result.toString();
	}

	/** Writes the page document content to file.
	 * @throws IOException
	 * @throws IllegalArgumentException If file already exists
	 */
	public void saveTo(final File file) {
		if(file.exists())
			throw new IllegalArgumentException("File '" + file.toString() + "' already exists.");

		try {
			saveTo(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			throw MechanizeExceptionFactory.newException(e);
		}
	}

	/** Writes the page document content to file.
	 * @throws IOException
	 * @throws IllegalArgumentException If file already exists
	 */
	public void saveTo(final OutputStream out) {
		try {
			Util.copy(getInputStream(), out);
		} catch (IOException e) {
			throw new MechanizeException(e);
		}
	}

	/**
	 * Useful for diagnostics
	 * 
	 * @return
	 */
	public String saveToString() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		saveTo(baos);
		return new String(baos.toByteArray());
	}

	@Override
	public String absoluteUrl(final String uri) {
		try {
			URL baseUrl = new URL(getUri());
			return new URL(baseUrl, uri).toExternalForm();
		} catch (MalformedURLException e) {
			throw MechanizeExceptionFactory.newException(e);
		}
	}
}
