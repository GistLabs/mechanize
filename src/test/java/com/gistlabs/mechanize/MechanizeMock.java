/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.form.FormParams;
import com.gistlabs.mechanize.form.FormParams.FormHttpParameter;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class MechanizeMock extends MechanizeAgent {
	
	private List<PageRequest> requests = new ArrayList<PageRequest>();
	
	public PageRequest addPageRequest(String uri, String html) {
		PageRequest request = new PageRequest(uri, html);
		requests.add(request);
		return request;
	}
	
	@Override
	protected HttpResponse execute(HttpClient client, HttpRequestBase request) throws IOException, ClientProtocolException {
		PageRequest pageRequest = nextUnexecutedPageRequest();
		if(pageRequest != null)
			return pageRequest.consume(client, request);
		else {
			Assert.fail("No open page requests");
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
		public final String uri;
		public final String html;
		public boolean wasExecuted = false;
		public HttpClient client = null;
		public HttpRequest request = null;
		public String charset = "utf-8";
		private String contentLocation = null;
		
		public PageRequest(String uri, String html) {
			this.uri = uri;
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
					
					this.wasExecuted = true;
					return response;
				}
				else {
					Assert.assertEquals("URI of the next PageRequest does not match", uri, request.getURI().toString());
					return null;
				}
					
			}
			else
				throw new UnsupportedOperationException("Request already executed");
		}

		private String getRequestUri(HttpRequestBase request) {
			if(request instanceof HttpGet && request.getParams() instanceof FormParams) {
				StringBuilder queryString = new StringBuilder();
				for(FormHttpParameter parameter : ((FormParams)request.getParams())) {
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
