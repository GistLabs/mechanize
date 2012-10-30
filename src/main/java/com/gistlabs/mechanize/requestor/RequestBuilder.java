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
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;

import com.gistlabs.mechanize.exceptions.MechanizeExceptionFactory;
import com.gistlabs.mechanize.headers.Header;
import com.gistlabs.mechanize.headers.Headers;
import com.gistlabs.mechanize.parameters.Parameter;
import com.gistlabs.mechanize.parameters.Parameters;
import com.gistlabs.mechanize.util.apache.URIBuilder;
import com.gistlabs.mechanize.util.apache.URLEncodedUtils;

public class RequestBuilder<Resource> {
	private final PageRequestor<Resource> requestor;
	private String uri;
	private final Parameters parameters = new Parameters();
	private final Headers setHeaders = new Headers();
	private final Headers addHeaders = new Headers();
	private final Map<String, ContentBody> files = new HashMap<String, ContentBody>();
	private boolean isMultiPart = false;

	public RequestBuilder(final PageRequestor<Resource> requestor) {
		this.requestor = requestor;
	}

	public RequestBuilder(final PageRequestor<Resource> requestor, final String uri) {
		this(requestor);
		setUri(uri);
	}

	private void setUri(final String uri) {
		this.uri = uri;

		if(uri.contains("?"))
			for(NameValuePair param : URLEncodedUtils.parse(uri.substring(uri.indexOf('?') + 1), Charset.forName("UTF-8")))
				parameters.add(param.getName(), param.getValue());
	}

	public RequestBuilder<Resource> multiPart() {
		this.isMultiPart = true;
		return this;
	}

	public RequestBuilder<Resource> add(final String name, final String ... values) {
		parameters.add(name, values);
		return this;
	}

	public RequestBuilder<Resource> set(final String name, final String ... values) {
		parameters.set(name, values);
		return this;
	}

	public RequestBuilder<Resource> set(final Parameters parameters) {
		for(String name : parameters.getNames())
			set(name, parameters.get(name));

		return this;
	}

	public RequestBuilder<Resource> add(final Parameters parameters) {
		for(String name : parameters.getNames())
			add(name, parameters.get(name));

		return this;
	}

	public RequestBuilder<Resource> addHeader(final String name, final String ... values) {
		addHeaders.add(name, values);
		return this;
	}

	public RequestBuilder<Resource> setHeader(final String name, final String ... values) {
		setHeaders.set(name, values);
		return this;
	}

	public RequestBuilder<Resource> setHeaders(final Headers headers) {
		for(String name : headers.getNames())
			setHeader(name, headers.get(name));

		return this;
	}

	public RequestBuilder<Resource> addHeaders(final Headers headers) {
		for(String name :headers.getNames())
			addHeader(name, headers.get(name));

		return this;
	}

	public RequestBuilder<Resource> accept(final String contentType) {
		this.setHeader("Accept", contentType);
		return this;
	}

	/** Adds a file to the request also making the request to become a multi-part post request or removes any file registered
	 *  under the given name if the file value is null. */
	public RequestBuilder<Resource> set(final String name, final File file) {
		return set(name, file != null ? new FileBody(file) : null);
	}

	/** Adds an ContentBody object. */
	public RequestBuilder<Resource> set(final String name, final ContentBody contentBody) {
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
		HttpRequestBase request = composeGetRequest(uri, parameters);
		buildHeaders(request);
		return requestor.request(request);
	}

	public <T extends Resource> T post() {
		HttpPost request = (!hasFiles()) || isMultiPart ? composePostRequest(getBaseUri(), parameters) :
			composeMultiPartFormRequest(getBaseUri(), parameters, files);
		buildHeaders(request);
		return requestor.request(request);
	}

	private boolean hasFiles() {
		return !files.isEmpty();
	}

	private String getBaseUri() {
		return uri.contains("?") ? uri.substring(0, uri.indexOf('?')) : uri;
	}

	private void buildHeaders(final HttpRequestBase request) {
		for(Header header : this.setHeaders)
			for(String value : header)
				request.setHeader(header.getName(), value);
		for(Header header : this.addHeaders)
			for(String value : header)
				request.addHeader(header.getName(), value);
	}

	private HttpRequestBase composeGetRequest(String uri, final Parameters parameters) {
		try {
			URIBuilder builder = new URIBuilder(uri);

			for(Parameter param : parameters)
				if(param.isSingleValue())
					builder.setParameter(param.getName(), param.getValue());
				else
					for(String value : param.getValues())
						builder.addParameter(param.getName(), value);

			URI requestURI = builder.build();
			uri = requestURI.toString();
			return new HttpGet(requestURI);
		}
		catch(URISyntaxException e) {
			throw MechanizeExceptionFactory.newException(e);
		}
	}

	private HttpPost composePostRequest(final String uri, final Parameters parameters) {
		HttpPost request = new HttpPost(uri);
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		for(Parameter param : parameters)
			if(param.isSingleValue())
				formparams.add(new BasicNameValuePair(param.getName(), param.getValue()));
			else
				for(String value : param.getValues())
					formparams.add(new BasicNameValuePair(param.getName(), value));
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
			request.setEntity(entity);
		} catch (UnsupportedEncodingException e) {
			throw MechanizeExceptionFactory.newException(e);
		}

		return request;
	}

	private HttpPost composeMultiPartFormRequest(final String uri, final Parameters parameters, final Map<String, ContentBody> files) {
		HttpPost request = new HttpPost(uri);
		MultipartEntity multiPartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

		try {
			Charset utf8 = Charset.forName("UTF-8");
			for(Parameter param : parameters)
				if(param.isSingleValue())
					multiPartEntity.addPart(param.getName(), new StringBody(param.getValue(), utf8));
				else
					for(String value : param.getValues())
						multiPartEntity.addPart(param.getName(), new StringBody(value, utf8));
		} catch (UnsupportedEncodingException e) {
			throw MechanizeExceptionFactory.newException(e);
		}

		List<String> fileNames = new ArrayList<String>(files.keySet());
		Collections.sort(fileNames);
		for(String name : fileNames) {
			ContentBody contentBody = files.get(name);
			multiPartEntity.addPart(name, contentBody);
		}
		request.setEntity(multiPartEntity);
		return request;
	}
}