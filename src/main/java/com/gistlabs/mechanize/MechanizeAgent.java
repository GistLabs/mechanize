/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;

import com.gistlabs.mechanize.cookie.Cookies;
import com.gistlabs.mechanize.exceptions.MechanizeExceptionFactory;
import com.gistlabs.mechanize.parameters.Parameters;
import com.gistlabs.mechanize.requestor.PageRequestor;
import com.gistlabs.mechanize.requestor.RequestBuilder;
import com.gistlabs.mechanize.requestor.RequestBuilderFactory;

/**
 * Mechanize agent acts as a focal point for HTTP interactions and also as a factory for Page objects from responses.
 * 
 * <p>Interesting resources: http://en.wikipedia.org/wiki/List_of_HTTP_header_fields</p>
 * 
 * <p>NOTE: The mechanize library is not synchronized and should be used in a single thread environment or with custom synchronization.</p>
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @author John Heintz <john@gistlabs.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class MechanizeAgent implements PageRequestor<Resource>, RequestBuilderFactory<Resource> {
	static String VERSION;
	
	static final Map<String,ResourceFactory> PAGE_FACTORIES = new HashMap<String, ResourceFactory>();
	static ResourceFactory lookupFactory(String mimeType) {
		return PAGE_FACTORIES.get(mimeType);
	}
	static void registerFactory(ResourceFactory factory) {
		Collection<String> contentMatches = factory.getContentMatches();
		for (String mimeType : contentMatches) {
			PAGE_FACTORIES.put(mimeType, factory);
		}
	}
	public static void setVersion(String version) {
		VERSION=version;
	}
	static {
		MechanizeInitializer.initialize();
	}

	private final AbstractHttpClient client;
	private final Cookies cookies;
	private final List<Interceptor> interceptors = new ArrayList<Interceptor>();
	private final History history = new History(this);

	public MechanizeAgent() {
		this.client = buildDefaultHttpClient();
		this.cookies = new Cookies(client);
	}
	
	public MechanizeAgent(AbstractHttpClient client) {
		this.client = client;
		this.cookies = new Cookies(client);
	}
	
	/**
	 * Configure the default HttpClient used by mechanize. 
	 */
	protected AbstractHttpClient buildDefaultHttpClient() {
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		return defaultHttpClient;
	}
	
	/**
	 * 
	 * @param userAgent The value to set User-Agent HTTP parameter to for requests
	 * @return
	 */
	public MechanizeAgent setUserAgent(String userAgent) {
		HttpProtocolParams.setUserAgent(this.client.getParams(), userAgent);
		return this;
	}
	
	/**
	 * 
	 * @return the User-Agent that HttpClient is currently using.
	 */
	public String getUserAgent() {
		return HttpProtocolParams.getUserAgent(this.client.getParams());
	}
	
	public AbstractHttpClient getClient() {
		return client;
	}
	
	public History history() {
		return history;
	}

	@Override
	public RequestBuilder<Resource> doRequest(String uri) {
		return new RequestBuilder<Resource>(this, uri);
	}

	/** 
	 * Returns the resource received uppon the request. The resource can be casted to any expected subclass of resource
	 * but will fail with ClassCastException if the expected type of resource is not the actual returned resource. 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Resource> T request(HttpRequestBase request) {
		try {
			HttpResponse response = execute(client, request);
			Resource resource = toPage(request, response);
			history.add(resource);
			return (T)resource;
		} catch (ClientProtocolException e) {
			throw MechanizeExceptionFactory.newException(e);
		} catch (IOException e) {
			throw MechanizeExceptionFactory.newException(e);
		}
	}
	
	public <T extends Resource> T get(String uri) {
		return doRequest(uri).get();
	}
	
	public <T extends Resource> T post(String uri, Map<String, String> params) throws UnsupportedEncodingException {
		return post(uri, new Parameters(unsafeCast(params)));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Object> unsafeCast(Map<String, String> params) {
		return (Map<String,Object>)(Map)params;
	}
	
	/**
	 * POST either URL encoded or multi-part encoded content body, based on presence of file content body parameters
	 * @param uri
	 * @param params
	 * @return
	 */
	public <T extends Resource> T post(String uri, Parameters params) {
		return doRequest(uri).set(params).post();
	}
	
	/** Idles / Waits for the given amount of milliseconds useful to prevent being blocked by mass sending 
	 *  requests or to appear as a artificial user. */
	public void idle(int milliseconds) {
		long startTime = System.currentTimeMillis();
		while((System.currentTimeMillis() - startTime) < milliseconds) {
			try {
				Thread.sleep(Math.max(1, milliseconds - (System.currentTimeMillis() - startTime)));
			}
			catch(InterruptedException e) {
			}
		}
	}
	
	public Cookies cookies() {
		return cookies;
	}	
	
	protected Resource toPage(HttpRequestBase request, HttpResponse response)
			throws IOException, UnsupportedEncodingException {
		ContentType contentType = getContentType(response);
		
		ResourceFactory factory = lookupFactory(contentType.getMimeType());
		if (factory == null)
			factory = lookupFactory(ContentType.WILDCARD.getMimeType());
		
		if (factory == null)
			throw MechanizeExceptionFactory.newMechanizeException("No viable page type found, and no wildcard mime type factory registered.");

		return factory.buildPage(this, request, response);
	}

	protected ContentType getContentType(HttpResponse response) {
		return ContentType.getOrDefault(response.getEntity());
	}

	protected HttpResponse execute(HttpClient client, HttpRequestBase request) throws IOException, ClientProtocolException {
		for(RequestInterceptor interceptor : filterInterceptors(RequestInterceptor.class))
			interceptor.intercept(this, request);
		HttpResponse response = client.execute(request);
		
		for(ResponseInterceptor interceptor : filterInterceptors(ResponseInterceptor.class))
			interceptor.intercept(this, response, request);
		return response;
	}
	
	public void addInterceptor(Interceptor interceptor) {
		if(!interceptors.contains(interceptor))
			interceptors.add(interceptor);
	}

	public void removeInterceptor(Interceptor interceptor) {
		interceptors.remove(interceptor);
	}

	private <T extends Interceptor> List<T> filterInterceptors(Class<T> clazz) {
		List<T> result = new ArrayList<T>();
		for(Interceptor interceptor : interceptors)
			if(clazz.isInstance(interceptor))
				result.add(clazz.cast(interceptor));
		return result;
	}
	
	@Override
	public String absoluteUrl(String uri) {
		try {
			return new URL(uri).toExternalForm();
		} catch (MalformedURLException e) {
			throw MechanizeExceptionFactory.newException(e);
		}
	}
}
