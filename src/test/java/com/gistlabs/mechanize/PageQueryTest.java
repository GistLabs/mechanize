/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import static com.gistlabs.mechanize.util.css.CSSHelper.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.gistlabs.mechanize.document.AbstractDocument;
import com.gistlabs.mechanize.document.link.Link;
import com.gistlabs.mechanize.util.apache.ContentType;

public class PageQueryTest extends MechanizeTestCase {

	protected String contentType() {
		return ContentType.TEXT_HTML.getMimeType();
	}
	

	@Test
	public void testLinkQueryById() {
		addPageRequest("http://test.com", 
				newHtml("Test Page", "<a id=\"foo\" href=\"foo.html\">foo</a>"));
		
		AbstractDocument page = agent().get("http://test.com");
		Link link = page.links().find("#foo");
		assertNotNull(link);
		assertEquals("http://test.com/foo.html", link.href());
	}

	@Test
	public void testLinkQueryByClass() {
		addPageRequest("http://test.com", 
				newHtml("Test Page", "<a id=\"foo\" class=\"bar baz\" href=\"foo.html\">foo</a>"));
		
		AbstractDocument page = agent().get("http://test.com");
		
		assertNull(page.links().find(".foo")); // foo class
		
		Link link1 = page.links().find(".bar");
		assertNotNull(link1);
		assertEquals("http://test.com/foo.html", link1.href());

		Link link2 = page.links().find(".baz");
		assertNotNull(link2);
		assertEquals("http://test.com/foo.html", link2.href());
	}

	@Test
	public void testLinkQueryByNameNotFound() {
		addPageRequest("http://test.com", 
				newHtml("Test Page", "<a id=\"foo\" href=\"foo.html\">foo</a>"));
		
		AbstractDocument page = agent().get("http://test.com");
		Link link = page.links().find(byName("foo"));
		assertNull(link);
	}

	@Test
	public void testLinkQueryByIdOrClass() {
		addPageRequest("http://test.com", 
				newHtml("Test Page", "<a id=\"foo\" class=\"bar baz\" href=\"foo.html\">foo</a>"));
		
		AbstractDocument page = agent().get("http://test.com");
		
		assertNotNull(page.links().find(byIdOrClass("foo")));
		assertNotNull(page.links().find(byIdOrClass("bar")));
		assertNotNull(page.links().find(byIdOrClass("baz")));
		
		assertNull(page.links().find(byIdOrClass("x")));
	}

	@Test
	public void testPageLinkQueryIsByIdOrClass() {
		addPageRequest("http://test.com", 
				newHtml("Test Page", "<a id=\"foo\" class=\"bar baz\" href=\"foo.html\">foo</a>"));
		
		AbstractDocument page = agent().get("http://test.com");
		
		assertNotNull(page.link("foo"));
		assertNotNull(page.link("bar"));
		assertNotNull(page.link("baz"));
		
		assertNull(page.link("x"));
	}

	
	@Test
	public void testFormQueryById() {
		addPageRequest("http://test.com", 
				newHtml("Test Page", "<form action=\"form\" id=\"form\"></form>"));
		
		AbstractDocument page = agent().get("http://test.com");
		
		assertNull(page.form("foo"));
		assertNotNull(page.form("form"));
	}

}
