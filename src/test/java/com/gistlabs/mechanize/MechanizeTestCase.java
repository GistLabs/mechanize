/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.After;

import com.gistlabs.mechanize.util.apache.ContentType;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class MechanizeTestCase {
	private MechanizeMock agent = new MechanizeMock();
	protected boolean doAfterTest = true;
	
	protected String contentType() {
		return ContentType.TEXT_HTML.getMimeType();
	}
	public void disableAfterTest() {
		doAfterTest = false;
	}

	@After
	public void afterTest() {
		if(doAfterTest) {
			PageRequest next = agent.nextUnexecutedPageRequest();
			if(next != null)
				Assert.fail("Unexecuted page request: " + next.toString());
		}
	}

	public PageRequest addPageRequest(final String uri, final String body) {
		return addPageRequest("GET", uri, body);
	}

	public PageRequest addPageRequest(final String method, final String uri, final String body) {
		PageRequest request = new PageRequest(method, uri, body).setContentType(contentType());
		agent.requests.add(request);
		return request;
	}

	public PageRequest addPageRequest(final String method, final String uri, final InputStream body) {
		PageRequest request = new PageRequest(method, uri, body).setContentType(contentType());
		agent.requests.add(request);
		return request;
	}

	protected String newHtml(final String title, final String bodyHtml) {
		return "<html><head><title>" + title + "</title></head><body>" + bodyHtml + "</body></html>";
	}

	public List<NameValuePair> parameter(final String name, final String value) {
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		result.add(new BasicNameValuePair(name, value));
		return result;
	}

	protected Mechanize agent() {
		return agent;
	}
}
