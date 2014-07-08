/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import static junit.framework.Assert.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import com.gistlabs.mechanize.impl.MechanizeAgent;
import com.gistlabs.mechanize.parameters.Parameters;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class MechanizeMock extends MechanizeAgent {

	final List<PageRequest> requests = new ArrayList<PageRequest>();

	@Deprecated
	public PageRequest addPageRequest(final String uri, final String body) {
		return addPageRequest("GET", uri, body);
	}

	@Deprecated
	public PageRequest addPageRequest(final String method, final String uri, final String body) {
		PageRequest request = new PageRequest(method, uri, body);
		requests.add(request);
		return request;
	}

	@Deprecated
	public PageRequest addPageRequest(final String method, final String uri, final InputStream body) {
		PageRequest request = new PageRequest(method, uri, body);
		requests.add(request);
		return request;
	}

	@Deprecated
	public PageRequest addPageRequest(final String method, final String uri, final Parameters parameters, final String body) {
		PageRequest request = new PageRequest(method, uri, parameters, body);
		requests.add(request);
		return request;
	}

	@Override
	protected HttpResponse execute(final HttpClient client, final HttpRequestBase request) throws Exception {
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
}
