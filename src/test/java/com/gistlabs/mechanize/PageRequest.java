/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.junit.Assert;
import org.junit.internal.ArrayComparisonFailure;

import com.gistlabs.mechanize.exceptions.MechanizeExceptionFactory;
import com.gistlabs.mechanize.headers.Header;
import com.gistlabs.mechanize.headers.Headers;
import com.gistlabs.mechanize.parameters.Parameters;
import com.gistlabs.mechanize.util.apache.ContentType;
import com.gistlabs.mechanize.util.apache.URLEncodedUtils;

public class PageRequest {
	public final String httpMethod;
	public final String uri;
	public Parameters parameters;

	public Headers headers = new Headers();
	public final InputStream body;
	public boolean wasExecuted = false;
	public HttpClient client = null;
	public HttpRequest request = null;
	public String contentType = ContentType.TEXT_HTML.getMimeType();
	public String charset = "utf-8";
	private String contentLocation = null;

	public PageRequest(final String uri, final String body) {
		this("GET", uri, body);
	}

	public PageRequest(final String method, final String uri, final String body) {
		this(method, uri, new Parameters(), body);
	}

	public PageRequest(final String method, final String uri, final InputStream body) {
		this(method, uri, new Parameters(), body);
	}

	public PageRequest(final String method, final String uri, final Parameters parameters, final String body) {
		this(method, uri, parameters, new ByteArrayInputStream(body.getBytes()));
	}

	public PageRequest(final String method, final String uri, final Parameters parameters, final InputStream body) {
		this.httpMethod = method;
		this.uri = uri;
		this.parameters = parameters;
		this.body = body;
	}

	public PageRequest setParameters(Parameters parameters) {
		this.parameters = parameters;
		return this;
	}
	public Parameters getParameters() {
		return parameters;
	}

	public PageRequest addHeader(final String header, final String... values) {
		this.headers.add(header, values);

		return this;
	}
	
	public PageRequest setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public boolean wasExecuted() {
		return wasExecuted;
	}

	public void setEncoding(final String charset) {
		this.charset = charset;
	}

	public PageRequest setContentLocation(final String contentLocation) {
		this.contentLocation = contentLocation;
		return this;
	}

	public HttpResponse consume(final HttpClient client, final HttpRequestBase request) throws Exception {
		if(!wasExecuted) {
			this.client = client;
			this.request = request;

			if(!request.getMethod().equalsIgnoreCase(httpMethod))
				throw new IllegalArgumentException(String.format("Expected %s, but was %s", httpMethod, request.getMethod()));

			if(request.getURI().toString().equals(uri)) {
				HttpResponse response = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), 200, "OK");
				BasicHttpEntity entity = new BasicHttpEntity();
				if(contentLocation != null)
					response.addHeader(new BasicHeader("Content-Location", contentLocation));
				entity.setContentEncoding(charset);
				entity.setContentType(this.contentType);
				entity.setContent(this.body);
				response.setEntity(new BufferedHttpEntity(entity));

				assertParameters(request);
				assertHeaders(request);

				this.wasExecuted = true;
				return response;
			}
			else {
				assertEquals("URI of the next PageRequest does not match", uri, request.getURI().toString());
				return null;
			}
		}
		else
			throw new UnsupportedOperationException("Request already executed");
	}

	private void assertHeaders(final HttpRequestBase request) throws ArrayComparisonFailure {
		for(Header header: headers) {
			org.apache.http.Header[] requestHeaders = request.getHeaders(header.getName());
			boolean foundMatch = false;
			for(org.apache.http.Header requestHeader : requestHeaders)
				if (header.getValues().contains(requestHeader.getValue()))
					foundMatch=true;
			if (!foundMatch)
				Assert.fail(String.format("Could not find request header matching: %s", header));
		}
		if(request instanceof HttpPost) {
			HttpPost post = (HttpPost)request;
			HttpEntity entity = post.getEntity();
			Parameters actualParameters = extractParameters(entity);

			String [] expectedNames = parameters.getNames();
			String [] actualNames = actualParameters.getNames();
			Arrays.sort(expectedNames);
			Arrays.sort(actualNames);
			Assert.assertArrayEquals("Expected and actual parameters should equal by available parameter names",
					expectedNames, actualNames);

			for(String name : expectedNames) {
				String [] expectedValue = parameters.get(name);
				String [] actualValue = actualParameters.get(name);
				Assert.assertArrayEquals("Expected parameter of next PageRequest '" + uri + "' must match", expectedValue, actualValue);
			}
		}
	}

	private void assertParameters(final HttpRequestBase request) throws ArrayComparisonFailure {
		if(request instanceof HttpPost) {
			HttpPost post = (HttpPost)request;
			HttpEntity entity = post.getEntity();
			Parameters actualParameters = extractParameters(entity);

			String [] expectedNames = parameters.getNames();
			String [] actualNames = actualParameters.getNames();
			Arrays.sort(expectedNames);
			Arrays.sort(actualNames);
			Assert.assertArrayEquals("Expected and actual parameters should equal by available parameter names",
					expectedNames, actualNames);

			for(String name : expectedNames) {
				String [] expectedValue = parameters.get(name);
				String [] actualValue = actualParameters.get(name);
				Assert.assertArrayEquals("Expected parameter of next PageRequest '" + uri + "' must match", expectedValue, actualValue);
			}
		}
	}

	private Parameters extractParameters(final HttpEntity entity) {
		if (entity instanceof UrlEncodedFormEntity)
			return extractParameters((UrlEncodedFormEntity)entity);
		if (entity instanceof MultipartEntity)
			return extractParameters((MultipartEntity)entity);
		throw new ClassCastException(String.format("Can't convert %s to either UrlEncodedFormEntity or MultipartEntity",entity.getClass()));
	}

	private Parameters extractParameters(final MultipartEntity entity) {
		Parameters parameters = new Parameters();

		// appears to be impossible to get parts...?

		return parameters;
	}

	private Parameters extractParameters(final UrlEncodedFormEntity entity) {
		try {
			InputStream stream = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			StringBuilder content = new StringBuilder();
			while(true) {
				String line = reader.readLine();
				if(line != null) {
					if(content.length() > 0)
						content.append("\n");
					content.append(line);
				}
				else
					break;
			}

			Parameters parameters = new Parameters();
			for(NameValuePair param : URLEncodedUtils.parse(content.toString(), Charset.forName("UTF-8")))
				parameters.add(param.getName(), param.getValue());
			return parameters;
		}
		catch(IOException e) {
			throw MechanizeExceptionFactory.newException(e);
		}
	}

	@Override
	public String toString() {
		return "<" + httpMethod + ":" + uri + "{" + parameters + "}" + ">";
	}

}