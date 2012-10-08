/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import static com.gistlabs.mechanize.query.QueryBuilder.*;

import java.io.*;
import java.util.Collection;
import java.util.Collections;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.gistlabs.mechanize.exceptions.MechanizeException;
import com.gistlabs.mechanize.exceptions.MechanizeExceptionFactory;
import com.gistlabs.mechanize.form.Form;
import com.gistlabs.mechanize.form.Forms;
import com.gistlabs.mechanize.html.JsoupDataUtil;
import com.gistlabs.mechanize.image.Images;
import com.gistlabs.mechanize.link.Link;
import com.gistlabs.mechanize.link.Links;
import com.gistlabs.mechanize.util.CopyInputStream;
import com.gistlabs.mechanize.util.NullOutputStream;
import com.gistlabs.mechanize.util.Util;

/** Represents an HTML page.
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public abstract class Page implements RequestBuilderFactory {
	
	@SuppressWarnings("unchecked")
	public static Collection<String> CONTENT_MATCHERS = Collections.EMPTY_LIST;

	private final MechanizeAgent agent;
	protected final String uri;
	private final HttpRequestBase request;
	protected final HttpResponse response;

	private ByteArrayOutputStream originalContent;
	private Links links;
	private Forms forms;
	private Images images;
	
	public Page(MechanizeAgent agent, HttpRequestBase request, HttpResponse response) {
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

	protected String getContentEncoding(HttpResponse response) {
		return JsoupDataUtil.getCharsetFromContentType(response.getEntity().getContentType());
	}

	/**
	 * This will return (and cache) the response entity content input stream, or return a stream from the previously cached value.
	 * 
	 * @return
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException {
		if (this.originalContent==null) {
			this.originalContent = new ByteArrayOutputStream(getIntContentLength(this.response));
			return new CopyInputStream(response.getEntity().getContent(), this.originalContent);
		} else { // use cached data
			return new ByteArrayInputStream(this.originalContent.toByteArray());
		}
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
	
	protected int getIntContentLength(HttpResponse response) {
		long longLength = response.getEntity().getContentLength();
		if (longLength<0)
			return 0;
		else if (longLength>Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		else
			return (int)longLength;
	}

	protected String inspectUri(HttpRequestBase request, HttpResponse response) {
		Header contentLocation = Util.findHeader(response, "content-location");
		if(contentLocation != null && contentLocation.getValue() != null)
			return contentLocation.getValue();
		else
			return request.getURI().toString();
	}

	public String getContentType() {
		return response.getEntity().getContentType().getValue();
	}

	@Override
	public RequestBuilder doRequest(String uri) {
		return getAgent().doRequest(uri);
	}

	/**
	 * 
	 * @return
	 */
	public String getTitle() {
		return "";
	}

	/**
	 *  Query for a matching link, find first match by either id or by class attributes.
	 * 
	 * @param query wrapped with byIdOrClass()
	 * @return first Link found
	 */
	public Link link(String query) {
		return links().get(byIdOrClass(query));
	}
	
	public Links links() {
		if(this.links == null) {
			this.links = loadLinks();
		}
		return this.links;
	}

	protected Links loadLinks() {
		return new Links(this);
	}

	/**
	 *  Query for a matching form, find first match by either id or by class attributes.
	 * 
	 * @param query wrapped with byIdOrClass()
	 * @return first Form found
	 */
	public Form form(String query) {
		return forms().get(byIdOrClassOrName(query));
	}

	public Forms forms() {
		if(this.forms == null) {
			this.forms = loadForms();
		}
		return this.forms;
	}

	protected Forms loadForms() {
		return new Forms(this);
	}

	public Images images() {
		if(this.images == null) {
			this.images = loadImages();
		}
		return this.images;
	}

	protected Images loadImages() {
		return new Images(this);
	}

	public String getUri() {
		return uri;
	}
	
	public int size() {
		return originalContent.size();
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
	public void saveTo(File file) {
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
	public void saveTo(OutputStream out) {		
		try {
			Util.copy(getInputStream(), out);
		} catch (IOException e) {
			throw new MechanizeException(e);
		}
	}
}
