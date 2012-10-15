/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import static com.gistlabs.mechanize.html.query.HtmlQueryBuilder.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.gistlabs.mechanize.link.Link;

public class PageQueryTest extends MechanizeTestCase {

	@Test
	public void testLinkQueryById() {
		agent.addPageRequest("http://test.com", 
				newHtml("Test Page", "<a id=\"foo\" href=\"foo.html\">foo</a>"));
		
		Resource page = agent.get("http://test.com");
		Link link = page.links().get(byId("foo"));
		assertNotNull(link);
		assertEquals("http://test.com/foo.html", link.href());
	}

	@Test
	public void testLinkQueryByClass() {
		agent.addPageRequest("http://test.com", 
				newHtml("Test Page", "<a id=\"foo\" class=\"bar baz\" href=\"foo.html\">foo</a>"));
		
		Resource page = agent.get("http://test.com");
		
		assertNull(page.links().get(byClass("foo"))); // foo class
		
		Link link1 = page.links().get(byClass("bar"));
		assertNotNull(link1);
		assertEquals("http://test.com/foo.html", link1.href());

		Link link2 = page.links().get(byClass("baz"));
		assertNotNull(link2);
		assertEquals("http://test.com/foo.html", link2.href());
	}

	@Test
	public void testLinkQueryByNameNotFound() {
		agent.addPageRequest("http://test.com", 
				newHtml("Test Page", "<a id=\"foo\" href=\"foo.html\">foo</a>"));
		
		Resource page = agent.get("http://test.com");
		Link link = page.links().get(byName("foo"));
		assertNull(link);
	}

	@Test
	public void testLinkQueryByIdOrClass() {
		agent.addPageRequest("http://test.com", 
				newHtml("Test Page", "<a id=\"foo\" class=\"bar baz\" href=\"foo.html\">foo</a>"));
		
		Resource page = agent.get("http://test.com");
		
		assertNotNull(page.links().get(byIdOrClass("foo")));
		assertNotNull(page.links().get(byIdOrClass("bar")));
		assertNotNull(page.links().get(byIdOrClass("baz")));
		
		assertNull(page.links().get(byIdOrClass("x")));
	}

	@Test
	public void testPageLinkQueryIsByIdOrClass() {
		agent.addPageRequest("http://test.com", 
				newHtml("Test Page", "<a id=\"foo\" class=\"bar baz\" href=\"foo.html\">foo</a>"));
		
		Resource page = agent.get("http://test.com");
		
		assertNotNull(page.link("foo"));
		assertNotNull(page.link("bar"));
		assertNotNull(page.link("baz"));
		
		assertNull(page.link("x"));
	}

	
	@Test
	public void testFormQueryById() {
		agent.addPageRequest("http://test.com", 
				newHtml("Test Page", newForm("form").id("form")));
		
		Resource page = agent.get("http://test.com");
		
		assertNull(page.form("foo"));
		assertNotNull(page.form("form"));
	}

}
