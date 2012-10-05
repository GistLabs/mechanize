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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.gistlabs.mechanize.cookie.Cookies;
import com.gistlabs.mechanize.exceptions.MechanizeClientProtocolException;
import com.gistlabs.mechanize.exceptions.MechanizeIOException;
import com.gistlabs.mechanize.history.History;
import com.gistlabs.mechanize.parameters.Parameters;

/**
 * Mechanize agent acts as a focal point for HTTP interactions and also as a factory for Page objects from responses.
 * 
 * <p>Mechanize supports different Page types, mapped by ContentType. The system property "mechanize.pagetypes" is a 
 * comma-separated list of classnames for the default Page types. Today this is com.gistlabs.mechanize.HtmlPage and 
 * com.gistlabs.mechanize.ContentPage. Modify this property ONLY if you want to change the default loaded Page types.
 * The system property "mechanize.pagetype.ext" is a also loaded, and provides the typical way for framework extenders
 * to add custom content types, or MechanizeAgent.registerPageType(Class).</p>
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
public class MechanizeAgent implements PageRequestor, RequestBuilderFactory {
		
	private AbstractHttpClient client;
	private final Cookies cookies;
	private final List<Interceptor> interceptors = new ArrayList<Interceptor>();
	private final History history = new History(this);

	public MechanizeAgent() {
		this(new DefaultHttpClient());
	}
	
	public MechanizeAgent(AbstractHttpClient client) {
		this.client = client;
		this.cookies = new Cookies(client);
	}
	
	public AbstractHttpClient getClient() {
		return client;
	}
	
	public History history() {
		return history;
	}

	@Override
	public RequestBuilder doRequest(String uri) {
		return new RequestBuilder(this, uri);
	}

	@Override
	public Page request(HttpRequestBase request) {
		try {
			HttpResponse response = execute(client, request);
			Page page = toPage(request, response);
			history.add(page);
			return page;
		} catch (ClientProtocolException e) {
			throw new MechanizeClientProtocolException(e);
		} catch (IOException e) {
			throw new MechanizeIOException(e);
		}
	}
	
	public Page get(String uri) {
		return doRequest(uri).get();
	}
	
	public Page post(String uri, Map<String, String> params) throws UnsupportedEncodingException {
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
	public Page post(String uri, Parameters params) {
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
	
	private Page toPage(HttpRequestBase request, HttpResponse response)
			throws IOException, UnsupportedEncodingException {
		ContentType contentType = getContentType(response);
		
		if (HtmlPage.CONTENT_MATCHERS.contains(contentType.getMimeType()))
			return new HtmlPage(this, request, response);
		else
			return new ContentPage(this, request, response);
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
}
