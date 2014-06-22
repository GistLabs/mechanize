/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.gistlabs.mechanize.exceptions.MechanizeException;
import com.gistlabs.mechanize.parameters.Parameters;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class MechanizeAgentTest extends MechanizeTestCase {
	static final String firefoxUserAgent = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.1) Gecko/20100122 firefox/3.6.1";

	protected MechanizeAgent agent() {
		return new MechanizeAgent().setUserAgent(firefoxUserAgent);
	}

	@Test
	public void testReceivingAPage() {
		agent.addPageRequest("http://test.com", newHtml("Test Page", ""));
		Resource page = agent.get("http://test.com");
		assertEquals("Test Page", page.getTitle());
	}

	@Test(expected=MechanizeException.class)
	public void testExpectPostButReceiveGetRequestFails() throws Exception {
		agent.addPageRequest("POST", "http://test.com/form", newHtml("OK", ""));
		disableAfterTest();

		Resource result = agent.get("http://test.com/form");
		assertEquals("OK", result.getTitle());
	}

	@Test
	public void testRequestParametersExtractedFromUri() {
		Parameters parameters = agent.doRequest("http://www.test.com/index.html?query=ab+cd&page=1").parameters();
		assertEquals("ab cd", parameters.get("query")[0]);
		assertEquals("1", parameters.get("page")[0]);
	}

	@Test
	public void testDoRequestGet() {
		agent.addPageRequest("GET", "http://test.com/index.html?query=ab+cd&page=1", newHtml("Test Page", ""));
		Resource page = agent.doRequest("http://test.com/index.html").add("query", "ab cd").add("page", "1").get();
		assertEquals("Test Page", page.getTitle());
	}

	@Test
	public void testDoRequestGetWithExistingQueryParameters() {
		agent.addPageRequest("GET", "http://test.com/index.html?query=ab+cd&page=2", newHtml("Test Page", ""));
		Resource page = agent.doRequest("http://test.com/index.html?query=ab+cd&page=1").set("page", "2").get();
		assertEquals("Test Page", page.getTitle());
	}

	@Test
	public void testDoRequestPostWithExistingQueryParameters() {
		Parameters expectedParameters = new Parameters().add("query","ab cd").add("page", "2");
		agent.addPageRequest("Post", "http://test.com/index.html", expectedParameters, newHtml("Test Page", ""));
		Resource page = agent.doRequest("http://test.com/index.html?query=ab+cd&page=1").set("page", "2").post();
		assertEquals("Test Page", page.getTitle());
	}

	@Test
	public void testDoRequestWithSetHeader() {
		agent.addPageRequest("GET", "http://test.com/index.html?query=ab+cd&page=1", newHtml("Test Page", "")).addHeader("foo", "bar");
		Resource page = agent.doRequest("http://test.com/index.html").add("query", "ab cd").add("page", "1").setHeader("foo", "x").setHeader("foo", "bar").get();
		assertEquals("Test Page", page.getTitle());
	}

	@Test
	public void testDoRequestWithAddHeaders() {
		agent.addPageRequest("GET", "http://test.com/index.html?query=ab+cd&page=1", newHtml("Test Page", "")).addHeader("foo", "bar", "baz");
		Resource page = agent.doRequest("http://test.com/index.html").add("query", "ab cd").add("page", "1").addHeader("foo", "bar", "baz").get();
		assertEquals("Test Page", page.getTitle());
	}

	@Test
	public void testDoRequestWithAccept() {
		agent.addPageRequest("GET", "http://test.com/index.html?query=ab+cd&page=1", newHtml("Test Page", "")).addHeader("Accept", "application/json");
		Resource page = agent.doRequest("http://test.com/index.html").add("query", "ab cd").add("page", "1").accept("application/json").get();
		assertEquals("Test Page", page.getTitle());
	}
}
