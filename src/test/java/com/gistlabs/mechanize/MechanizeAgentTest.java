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

	@Test
	public void testPostMethod() {
		MechanizeAgent agent = agent();
		Parameters parameters = new Parameters().add("param1", "value").add("param2", "value2");
		Resource page = agent.post("http://posttestserver.com/post.php", parameters);
		String pageString = page.asString();
		assertTrue(pageString.contains(" Successfully dumped 2 post variables"));
	}

	@Test
	public void testDownloadToImage() throws IOException {
		String wikipediaLogoUri = "http://upload.wikimedia.org/wikipedia/commons/6/63/Wikipedia-logo.png";
		BufferedImage image = ImageIO.read(agent().get(wikipediaLogoUri).getInputStream());

		assertEquals(200, image.getWidth());
		assertEquals(200, image.getHeight());
	}

	@Test
	public void testDownloadToFile() throws Exception {
		String wikipediaLogoUri = "http://upload.wikimedia.org/wikipedia/commons/6/63/Wikipedia-logo.png";
		File file = File.createTempFile("mechanize", ".png");
		file.delete();

		agent().get(wikipediaLogoUri).saveTo(file);
		assertEquals(45283, file.length());
		file.delete();
	}


	@Test
	public void testDownloadPage() throws Exception {
		String wikipediaLogoUri = "http://upload.wikimedia.org/wikipedia/commons/6/63/Wikipedia-logo.png";
		File file = File.createTempFile("mechanize", ".png");
		file.delete();

		Resource page = agent().get(wikipediaLogoUri);

		assertTrue(page instanceof DefaultResource);

		assertEquals("image/png", page.getContentType());
		page.saveTo(file);
		assertEquals(45283, file.length());
		file.delete();

		// in here my copied stream doesn't get the right bytes...

		page.saveTo(file);
		assertEquals(45283, file.length());
		file.delete();
	}
}
