/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.html;

import static com.gistlabs.mechanize.query.QueryBuilder.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.gistlabs.mechanize.MechanizeTestCase;
import com.gistlabs.mechanize.Page;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class HtmlElementTest extends MechanizeTestCase {
	
	@Test
	public void testFindingALink() {
		agent.addPageRequest("http://test.com", 
				newHtml("Test Page", "<a href=\"http://test.com/myPage.html\">myPage</a>"));
		
		Page page = agent.get("http://test.com");
		HtmlElement htmlElement = ((HtmlPage)page).htmlElements().get(byTag("body")).get(byHRef(regEx(".*myPage.html")));
		assertNotNull(htmlElement);
		assertEquals("http://test.com/myPage.html", htmlElement.getAttribute("href"));
	}

	@Test
	public void testFindingTwoLinks() {
		agent.addPageRequest("http://test.com", 
				newHtml("Test Page", "<a href=\"link1\">link1</a><a href=\"link2\">link2</a>"));
		
		Page page = agent.get("http://test.com");
		List<HtmlElement> elements = ((HtmlPage)page).htmlElements().get(byTag("body")).getAll(byHRef(regEx("link[0-9]")));
		assertNotNull(elements);
		assertEquals(2, elements.size());
		assertEquals("link1", elements.get(0).getAttribute("href"));
		assertEquals("link2", elements.get(1).getAttribute("href"));
	}
}