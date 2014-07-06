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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

import org.apache.http.Header;
import org.apache.http.HttpMessage;
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
public abstract class AbstractResource implements RequestBuilderFactory<Resource>, Resource {

	@SuppressWarnings("unchecked")
	public static Collection<String> CONTENT_MATCHERS = Collections.EMPTY_LIST;

	private final Mechanize agent;
	private final String uri;
	private final HttpRequestBase request;
	protected final HttpResponse response;

	public AbstractResource(final Mechanize agent, final HttpRequestBase request, final HttpResponse response) {
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

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.Resource#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		return this.response.getEntity().getContent();
	}

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.Resource#getLength()
	 */
	@Override
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

		Header mechanizeLocation = Util.findHeader(response, Mechanize.MECHANIZE_LOCATION);
		if (mechanizeLocation!=null && mechanizeLocation.getValue()!=null)
			return mechanizeLocation.getValue();

		return request.getURI().toString();
	}

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.Resource#getContentType()
	 */
	@Override
	public String getContentType() {
		return response.getEntity().getContentType().getValue();
	}

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.Resource#doRequest(java.lang.String)
	 */
	@Override
	public RequestBuilder<Resource> doRequest(final String uri) {
		return getAgent().doRequest(uri);
	}

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.Resource#getTitle()
	 */
	@Override
	public String getTitle() {
		return "";
	}

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.Resource#getUri()
	 */
	@Override
	public String getUri() {
		return uri;
	}

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.Resource#size()
	 */
	@Override
	public long size() {
		return this.response.getEntity().getContentLength();
	}

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.Resource#getRequest()
	 */
	@Override
	public HttpRequestBase getRequest() {
		return request;
	}

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.Resource#getResponse()
	 */
	@Override
	public HttpResponse getResponse() {
		return response;
	}

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.Resource#getAgent()
	 */
	@Override
	public Mechanize getAgent() {
		return agent;
	}

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.Resource#asString()
	 */
	@Override
	public String asString() {
		ByteArrayOutputStream result = new ByteArrayOutputStream(getIntContentLength(this.response));
		saveTo(result);
		return result.toString();
	}

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.Resource#saveTo(java.io.File)
	 */
	@Override
	public void saveTo(final File file) {
		if(file.exists())
			throw new IllegalArgumentException("File '" + file.toString() + "' already exists.");

		try {
			saveTo(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			throw MechanizeExceptionFactory.newException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.Resource#saveTo(java.io.OutputStream)
	 */
	@Override
	public void saveTo(final OutputStream out) {
		try {
			Util.copy(getInputStream(), out);
		} catch (IOException e) {
			throw new MechanizeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.gistlabs.mechanize.Resource#saveToString()
	 */
	@Override
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

	@Override
	public String toString() {
		StringWriter result = new StringWriter();
		PrintWriter writer = new PrintWriter(result);

		writer.println("== Request ==");
		writer.println(getRequest().getRequestLine().toString());
		write(getRequest(), writer);

		writer.println();
		writer.println("== Response ==");
		writer.println(getResponse().getStatusLine().toString());
		write(getResponse(), writer);

		return result.toString();
	}

	private void write(final HttpMessage message, final PrintWriter writer) {
		Header[] headers = message.getAllHeaders();
		for (Header header : headers)
			writer.println(header.toString());
	}

}
