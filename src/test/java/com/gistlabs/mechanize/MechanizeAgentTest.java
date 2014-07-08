/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.gistlabs.mechanize.exceptions.MechanizeException;
import com.gistlabs.mechanize.parameters.Parameters;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class MechanizeAgentTest extends MechanizeTestCase {

	@Test
	public void testReceivingAPage() {
		addPageRequest("http://test.com", newHtml("Test Page", ""));
		Resource page = agent().get("http://test.com");
		assertEquals("Test Page", page.getTitle());
	}

	@Test(expected=MechanizeException.class)
	public void testExpectPostButReceiveGetRequestFails() throws Exception {
		addPageRequest("POST", "http://test.com/form", newHtml("OK", ""));
		disableAfterTest();

		Resource result = agent().get("http://test.com/form");
		assertEquals("OK", result.getTitle());
	}

	@Test
	public void testRequestParametersExtractedFromUri() {
		Parameters parameters = agent().doRequest("http://www.test.com/index.html?query=ab+cd&page=1").parameters();
		assertEquals("ab cd", parameters.get("query")[0]);
		assertEquals("1", parameters.get("page")[0]);
	}

	@Test
	public void testDoRequestGet() {
		addPageRequest("GET", "http://test.com/index.html?query=ab+cd&page=1", newHtml("Test Page", ""));
		Resource page = agent().doRequest("http://test.com/index.html").add("query", "ab cd").add("page", "1").get();
		assertEquals("Test Page", page.getTitle());
	}

	@Test
	public void testDoRequestGetWithExistingQueryParameters() {
		addPageRequest("GET", "http://test.com/index.html?query=ab+cd&page=2", newHtml("Test Page", ""));
		Resource page = agent().doRequest("http://test.com/index.html?query=ab+cd&page=1").set("page", "2").get();
		assertEquals("Test Page", page.getTitle());
	}

	@Test
	public void testDoRequestPostWithExistingQueryParameters() {
		Parameters expectedParameters = new Parameters().add("query","ab cd").add("page", "2");
		addPageRequest("Post", "http://test.com/index.html", newHtml("Test Page", "")).setParameters(expectedParameters);
		Resource page = agent().doRequest("http://test.com/index.html?query=ab+cd&page=1").set("page", "2").post();
		assertEquals("Test Page", page.getTitle());
	}

	@Test
	public void testDoRequestWithSetHeader() {
		addPageRequest("GET", "http://test.com/index.html?query=ab+cd&page=1", newHtml("Test Page", "")).addHeader("foo", "bar");
		Resource page = agent().doRequest("http://test.com/index.html").add("query", "ab cd").add("page", "1").setHeader("foo", "x").setHeader("foo", "bar").get();
		assertEquals("Test Page", page.getTitle());
	}

	@Test
	public void testDoRequestWithAddHeaders() {
		addPageRequest("GET", "http://test.com/index.html?query=ab+cd&page=1", newHtml("Test Page", "")).addHeader("foo", "bar", "baz");
		Resource page = agent().doRequest("http://test.com/index.html").add("query", "ab cd").add("page", "1").addHeader("foo", "bar", "baz").get();
		assertEquals("Test Page", page.getTitle());
	}

	@Test
	public void testDoRequestWithAccept() {
		addPageRequest("GET", "http://test.com/index.html?query=ab+cd&page=1", newHtml("Test Page", "")).addHeader("Accept", "application/json");
		Resource page = agent().doRequest("http://test.com/index.html").add("query", "ab cd").add("page", "1").accept("application/json").get();
		assertEquals("Test Page", page.getTitle());
	}
}
