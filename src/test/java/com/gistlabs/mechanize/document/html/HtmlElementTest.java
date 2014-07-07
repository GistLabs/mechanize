/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.gistlabs.mechanize.MechanizeTestCase;
import com.gistlabs.mechanize.Resource;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class HtmlElementTest extends MechanizeTestCase {
	
	@Test
	public void testFindingALink() {
		agent.addPageRequest("http://test.com", 
				newHtml("Test Page", "<a href=\"http://test.com/myPage.html\">myPage</a>"));
		
		Resource page = agent.get("http://test.com");
		HtmlElement htmlElement = ((HtmlDocument)page).htmlElements().find("body *[href$='myPage.html']");
		assertNotNull(htmlElement);
		assertEquals("http://test.com/myPage.html", htmlElement.getAttribute("href"));
	}

	@Test
	public void testFindingTwoLinks() {
		agent.addPageRequest("http://test.com", 
				newHtml("Test Page", "<a href=\"link1\">link1</a><a href=\"link2\">link2</a>"));
		
		Resource page = agent.get("http://test.com");
		List<HtmlElement> elements = ((HtmlDocument)page).htmlElements().findAll("body a[href*='link']");
		assertNotNull(elements);
		assertEquals(2, elements.size());
		assertEquals("link1", elements.get(0).getAttribute("href"));
		assertEquals("link2", elements.get(1).getAttribute("href"));
	}
}