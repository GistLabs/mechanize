package com.gistlabs.mechanize;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.gistlabs.mechanize.requestor.RequestBuilder;

public interface Resource {

	/**
	 * This will return (and cache) the response entity content input stream, or return a stream from the previously cached value.
	 * 
	 * @return
	 * @throws IOException
	 */
	public abstract InputStream getInputStream() throws IOException;

	/**
	 * (From HttpEntity) Tells the length of the content, if known.
	 *
	 * @return  the number of bytes of the content, or
	 *          a negative number if unknown. If the content length is known
	 *          but exceeds {@link java.lang.Long#MAX_VALUE Long.MAX_VALUE},
	 *          a negative number is returned.
	 */
	public abstract long getLength();

	public abstract String getContentType();

	public abstract RequestBuilder<Resource> doRequest(String uri);

	public String absoluteUrl(final String uri);

	/**
	 * 
	 * @return
	 */
	public abstract String getTitle();

	public abstract String getUri();

	public abstract long size();

	public abstract HttpRequestBase getRequest();

	public abstract HttpResponse getResponse();

	public abstract Mechanize getAgent();

	/**
	 * Serialize the contents of this page into a string
	 * 
	 * @return
	 */
	public abstract String asString();

	/** Writes the page document content to file.
	 * @throws IOException
	 * @throws IllegalArgumentException If file already exists
	 */
	public abstract void saveTo(File file);

	/** Writes the page document content to file.
	 * @throws IOException
	 * @throws IllegalArgumentException If file already exists
	 */
	public abstract void saveTo(OutputStream out);

	/**
	 * Useful for diagnostics
	 * 
	 * @return
	 */
	public abstract String saveToString();

}