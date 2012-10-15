/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.requestor;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;

import com.gistlabs.mechanize.exceptions.MechanizeExceptionFactory;
import com.gistlabs.mechanize.parameters.Parameter;
import com.gistlabs.mechanize.parameters.Parameters;

public class RequestBuilder<Resource> {
	private final PageRequestor<Resource> requestor;
	private String uri;
	private final Parameters parameters = new Parameters();
	private final Map<String, ContentBody> files = new HashMap<String, ContentBody>();
	private boolean isMultiPart = false;

	public RequestBuilder(PageRequestor<Resource> requestor) {
		this.requestor = requestor;
	}
	
	public RequestBuilder(PageRequestor<Resource> requestor, String uri) {
		this(requestor);
		setUri(uri);
	}

	private void setUri(String uri) {
		this.uri = uri;
		
		if(uri.contains("?")) 
			for(NameValuePair param : URLEncodedUtils.parse(uri.substring(uri.indexOf('?') + 1), Charset.forName("UTF-8"))) 
				parameters.add(param.getName(), param.getValue());
	}
	
	public RequestBuilder<Resource> multiPart() {
		this.isMultiPart = true;
		return this;
	}
	
	public RequestBuilder<Resource> add(String name, String ... values) {
		parameters.add(name, values);
		return this;
	}
	
	public RequestBuilder<Resource> set(String name, String ... values) {
		parameters.set(name, values);
		return this;
	}
	
	public RequestBuilder<Resource> set(Parameters parameters) {
		for(String name : parameters.getNames()) 
			set(name, parameters.get(name));
		
		return this;
	}

	public RequestBuilder<Resource> add(Parameters parameters) {
		for(String name :parameters.getNames()) 
			add(name, parameters.get(name));
		
		return this;
	}
	
	/** Adds a file to the request also making the request to become a multi-part post request or removes any file registered
	 *  under the given name if the file value is null. */
	public RequestBuilder<Resource> set(String name, File file) {
		return set(name, file != null ? new FileBody(file) : null);
	}
	
	/** Adds an ContentBody object. */
	public RequestBuilder<Resource> set(String name, ContentBody contentBody) {
		if(contentBody != null)
			files.put(name, contentBody);
		else
			files.remove(name);
		return this;
	}
	
	
	
	public Parameters parameters() {
		return parameters;
	}
	
	public <T extends Resource> T get() {
		if(hasFiles())
			throw new UnsupportedOperationException("Files can not be send using a get request");
		return requestor.request(composeGetRequest(uri, parameters));
	}
	
	public <T extends Resource> T post() {
		HttpPost request = (!hasFiles()) || isMultiPart ? composePostRequest(getBaseUri(), parameters) : 
			composeMultiPartFormRequest(getBaseUri(), parameters, files);
		return requestor.request(request);
	}
	
	private boolean hasFiles() {
		return !files.isEmpty();
	}

	private String getBaseUri() {
		return uri.contains("?") ? uri.substring(0, uri.indexOf('?')) : uri;
	}
	
	private HttpRequestBase composeGetRequest(String uri, Parameters parameters) {
		try {
			URIBuilder builder = new URIBuilder(uri);
			
			for(Parameter param : parameters) {
				if(param.isSingleValue())
					builder.setParameter(param.getName(), param.getValue());
				else
					for(String value : param.getValues())
						builder.addParameter(param.getName(), value);
			}
			
			URI requestURI = builder.build();
			uri = requestURI.toString();
			return new HttpGet(requestURI);
		}
		catch(URISyntaxException e) {
			throw MechanizeExceptionFactory.newException(e); 
		}
	}
	
	private HttpPost composePostRequest(String uri, Parameters parameters) {
		HttpPost request = new HttpPost(uri);
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		for(Parameter param : parameters) {
			if(param.isSingleValue())
				formparams.add(new BasicNameValuePair(param.getName(), param.getValue()));
			else {
				for(String value : param.getValues())
					formparams.add(new BasicNameValuePair(param.getName(), value));
			}
		}
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
			request.setEntity(entity);
		} catch (UnsupportedEncodingException e) {
			throw MechanizeExceptionFactory.newException(e); 
		}
		
		return request;
	}

	private HttpPost composeMultiPartFormRequest(String uri, Parameters parameters, Map<String, ContentBody> files) {
		HttpPost request = new HttpPost(uri);
		MultipartEntity multiPartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		
		try {
			Charset utf8 = Charset.forName("UTF-8");
			for(Parameter param : parameters) {
				if(param.isSingleValue())
					multiPartEntity.addPart(param.getName(), new StringBody(param.getValue(), utf8));
				else 
					for(String value : param.getValues())
							multiPartEntity.addPart(param.getName(), new StringBody(value, utf8));
			}
		} catch (UnsupportedEncodingException e) {
			throw MechanizeExceptionFactory.newException(e); 
		}

		List<String> fileNames = new ArrayList<String>(files.keySet());
		Collections.sort(fileNames);
		for(String name : fileNames) {
			ContentBody contentBody = files.get(name);
			multiPartEntity.addPart(name, contentBody);
		}
		((HttpPost)request).setEntity(multiPartEntity);
		return request;
	}
}