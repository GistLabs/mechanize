/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.junit.Assert;
import org.junit.internal.ArrayComparisonFailure;

import com.gistlabs.mechanize.Parameters.FormHttpParameter;
import com.gistlabs.mechanize.exceptions.MechanizeIOException;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class MechanizeMock extends MechanizeAgent {
	
	private List<PageRequest> requests = new ArrayList<PageRequest>();
	
	public PageRequest addPageRequest(String uri, String html) {
		return addPageRequest("GET", uri, html);
	}

	public PageRequest addPageRequest(String method, String uri, String html) {
		PageRequest request = new PageRequest(method, uri, html);
		requests.add(request);
		return request;
	}

	public PageRequest addPageRequest(String method, String uri, Parameters parameters, String html) {
		PageRequest request = new PageRequest(method, uri, parameters, html);
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
		public final String html;
		public boolean wasExecuted = false;
		public HttpClient client = null;
		public HttpRequest request = null;
		public String charset = "utf-8";
		private String contentLocation = null;
		
		public PageRequest(String uri, String html) {
			this("GET", uri, html);
		}
		
		public PageRequest(String method, String uri, String html) {
			this.httpMethod = method;
			this.uri = uri;
			this.parameters = new Parameters();
			this.html = html;
		}
		
		public PageRequest(String method, String uri, Parameters parameters, String html) {
			this.httpMethod = method;
			this.uri = uri;
			this.parameters = parameters;
			this.html = html;
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
				
				if(getRequestUri(request).equals(uri)) {
					HttpResponse response = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), 200, "OK");
					BasicHttpEntity entity = new BasicHttpEntity();
					response.setEntity(entity);
					if(contentLocation != null)
						response.addHeader(new BasicHeader("Content-Location", contentLocation));
					entity.setContentEncoding(charset);
					try {
						entity.setContent(new ByteArrayInputStream(html.getBytes(charset)));
					}
					catch(UnsupportedEncodingException e) {
						throw new UnsupportedOperationException("Encoding not supported", e);
					}
					
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
				throw new MechanizeIOException(e);
			}
		}

		private String getRequestUri(HttpRequestBase request) {
			if(request instanceof HttpGet && request.getParams() instanceof Parameters) {
				StringBuilder queryString = new StringBuilder();
				for(FormHttpParameter parameter : ((Parameters)request.getParams())) {
					if(queryString.length() > 0)
						queryString.append("&");
					String name = parameter.getName();
					String value = parameter.getValue();
					queryString.append(name + "=" + (value != null ? value : ""));
				}
				return request.getURI().toString() + "?" + queryString.toString();
			}
			else 
				return request.getURI().toString();
				
		}
	}
}
