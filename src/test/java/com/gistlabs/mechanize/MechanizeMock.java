/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import static junit.framework.Assert.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.junit.Assert;
import org.junit.internal.ArrayComparisonFailure;

import com.gistlabs.mechanize.exceptions.MechanizeExceptionFactory;
import com.gistlabs.mechanize.parameters.Parameters;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class MechanizeMock extends MechanizeAgent {
	
	private List<PageRequest> requests = new ArrayList<PageRequest>();
	
	public PageRequest addPageRequest(String uri, String body) {
		return addPageRequest("GET", uri, body);
	}

	public PageRequest addPageRequest(String method, String uri, String body) {
		PageRequest request = new PageRequest(method, uri, body);
		requests.add(request);
		return request;
	}

	public PageRequest addPageRequest(String method, String uri, InputStream body) {
		PageRequest request = new PageRequest(method, uri, body);
		requests.add(request);
		return request;
	}

	public PageRequest addPageRequest(String method, String uri, Parameters parameters, String body) {
		PageRequest request = new PageRequest(method, uri, parameters, body);
		requests.add(request);
		return request;
	}
	
	@Override
	protected HttpResponse execute(HttpClient client, HttpRequestBase request) throws IOException, ClientProtocolException {
		PageRequest pageRequest = nextUnexecutedPageRequest();
		if(pageRequest != null)
			return pageRequest.consume(client, request);
		else {
			fail("No open page requests");
			return null;
		}
	}
	
	public PageRequest nextUnexecutedPageRequest() {
		for(PageRequest pageRequest : requests)
			if(!pageRequest.wasExecuted())
				return pageRequest;
		return null;
	}
	
	public class PageRequest {
		public final String httpMethod;
		public final String uri;
		public final Parameters parameters;
		public final InputStream body;
		public boolean wasExecuted = false;
		public HttpClient client = null;
		public HttpRequest request = null;
		public String contentType = ContentType.TEXT_HTML.getMimeType();
		public String charset = "utf-8";
		private String contentLocation = null;
		
		public PageRequest(String uri, String body) {
			this("GET", uri, body);
		}
		
		public PageRequest(String method, String uri, String body) {
			this(method, uri, new Parameters(), body);
		}
		
		public PageRequest(String method, String uri, InputStream body) {
			this(method, uri, new Parameters(), body);
		}
		
		public PageRequest(String method, String uri, Parameters parameters, String body) {
			this(method, uri, parameters, new ByteArrayInputStream(body.getBytes()));
		}
		
		public PageRequest(String method, String uri, Parameters parameters, InputStream body) {
			this.httpMethod = method;
			this.uri = uri;
			this.parameters = parameters;
			this.body = body;
		}

		public boolean wasExecuted() {
			return wasExecuted;
		}
		
		public void setEncoding(String charset) {
			this.charset = charset;
		}
		
		public void setContentLocation(String contentLocation) {
			this.contentLocation = contentLocation;
		}
		
		public HttpResponse consume(HttpClient client, HttpRequestBase request) {
			if(!wasExecuted) {
				this.client = client;
				this.request = request;
				
				if(!request.getMethod().equalsIgnoreCase(httpMethod)) {
					throw new IllegalArgumentException(String.format("Expected %s, but was %s", httpMethod, request.getMethod()));
				}
				
				if(request.getURI().toString().equals(uri)) {
					HttpResponse response = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), 200, "OK");
					BasicHttpEntity entity = new BasicHttpEntity();
					response.setEntity(entity);
					if(contentLocation != null)
						response.addHeader(new BasicHeader("Content-Location", contentLocation));
					entity.setContentEncoding(charset);
					entity.setContentType(this.contentType);
					entity.setContent(body);
					
					assertParameters(request);
					
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

		private void assertParameters(HttpRequestBase request) throws ArrayComparisonFailure {
			if(request instanceof HttpPost) {
				HttpPost post = (HttpPost)request;
				UrlEncodedFormEntity entity = (UrlEncodedFormEntity)post.getEntity();
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

		private Parameters extractParameters(UrlEncodedFormEntity entity) {
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

		public void setContentType(String mimeType) {
			this.contentType = mimeType;	
		}
	}
}
