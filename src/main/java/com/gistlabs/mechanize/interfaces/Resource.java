/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.interfaces;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Represents a resource being reseived by a request.
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public interface Resource {

	/**
	 * Either the single header link with name, null if none found, or exception if more than one found.
	 * 
	 * @param name
	 * @return
	 */
	public Link headerLink(String name);

	/**
	 * All header links regardless of name
	 * @return
	 */
	public List<Link> headerLinks();

	/**
	 * All header links with name
	 * 
	 * @param name
	 * @return
	 */
	public List<Link> headerLinks(String name);


	/**
	 * This will return (and cache) the response entity content input stream, or return a stream from the previously cached value.
	 * 
	 * @return
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException;

	/**
	 * (From HttpEntity) Tells the length of the content, if known.
	 *
	 * @return  the number of bytes of the content, or
	 *          a negative number if unknown. If the content length is known
	 *          but exceeds {@link java.lang.Long#MAX_VALUE Long.MAX_VALUE},
	 *          a negative number is returned.
	 */
	public long getLength();

	public String getContentType();

	/**
	 * 
	 * @return
	 */
	public String getTitle();

	public String getUri();


	public HttpRequestBase getRequest();

	public HttpResponse getResponse();

	public Mechanize getAgent();

	/** Writes the page document content to file.
	 * @throws IOException
	 * @throws IllegalArgumentException If file already exists
	 */
	public void saveTo(final File file);

	/** Writes the page document content to file.
	 * @throws IOException
	 * @throws IllegalArgumentException If file already exists
	 */
	public void saveTo(final OutputStream out);

	/**
	 * Useful for diagnostics
	 * 
	 * @return
	 */
	public String saveToString();
}
