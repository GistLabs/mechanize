/*
 * Copyright (C) 2011 Gist Labs, LLC.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.gistlabs.mechanize.link;

import static org.junit.Assert.assertEquals;
import static com.gistlabs.mechanize.QueryBuilder.*;

import static org.junit.Assert.assertNotNull;
import com.gistlabs.mechanize.MechanizeTestCase;
import com.gistlabs.mechanize.Page;
import com.gistlabs.mechanize.MechanizeMock.PageRequest;
import com.gistlabs.mechanize.link.Link;

import org.junit.Test;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class LinksTest extends MechanizeTestCase {
	@Test
	public void testFollowingAnAbsoluteLink() {
		agent.addPageRequest("http://test.com", 
				newHtml("Test Page", "<a href=\"http://test.com/myPage.html\">myPage</a>"));
		agent.addPageRequest("http://test.com/myPage.html", newHtml("My Page", ""));
		
		Page page = agent.get("http://test.com");
		Link link = page.links().get(byInnerHtml("myPage"));
		assertNotNull(link);
		Page myPage = link.click();
		assertEquals("My Page", myPage.getTitle());
	}

	@Test
	public void testFollowingAnRelativeLink() {
		agent.addPageRequest("http://test.com", 
				newHtml("Test Page", "<a href=\"myPage.html\">myPage</a>"));
		agent.addPageRequest("http://test.com/myPage.html", newHtml("My Page", ""));
		
		Page page = agent.get("http://test.com");
		Link link = page.links().get(byInnerHtml("myPage"));
		assertNotNull(link);
		Page myPage = link.click();
		assertEquals("My Page", myPage.getTitle());
	}
	
	@Test
	public void testFollowingAnRelativeLinkWithBaseSetInHtmlPage() {
		agent.addPageRequest("http://test.com", 
				"<html><head><base href=\"http://www1.test.com\"/></head><body><a href=\"myPage.html\">myPage</a></body></html>");
		agent.addPageRequest("http://www1.test.com/myPage.html", newHtml("My Page", ""));
		
		Page page = agent.get("http://test.com");
		Link link = page.links().get(byInnerHtml("myPage"));
		assertNotNull(link);
		Page myPage = link.click();
		assertEquals("My Page", myPage.getTitle());
	}

	@Test
	public void testFollowingAnRelativeLinkWithContentLocationSetInHtmlResponseHeader() {
		PageRequest pageRequest = agent.addPageRequest("http://test.com", 
				newHtml("Test Page", "<a href=\"myPage.html\">myPage</a>"));
		pageRequest.setContentLocation("http://www1.test.com");
		agent.addPageRequest("http://www1.test.com/myPage.html", newHtml("My Page", ""));
		
		Page page = agent.get("http://test.com");
		Link link = page.links().get(byInnerHtml("myPage"));
		assertNotNull(link);
		Page myPage = link.click();
		assertEquals("My Page", myPage.getTitle());
	}
}