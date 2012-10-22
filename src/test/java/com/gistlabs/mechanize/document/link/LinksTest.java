/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.link;

import static com.gistlabs.mechanize.document.html.query.HtmlQueryBuilder.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.gistlabs.mechanize.MechanizeMock.PageRequest;
import com.gistlabs.mechanize.MechanizeTestCase;
import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.Page;
import com.gistlabs.mechanize.document.link.Link;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
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
		Resource myPage = link.click();
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
		Resource myPage = link.click();
		assertEquals("My Page", myPage.getTitle());
	}
	
	@Test
	public void testFollowingAnRelativeLinkWithBaseSetInHtmlPage() {
		agent.addPageRequest("http://test.com", 
				"<html><head><base href=\"http://www1.test.com\"/></head><body><a href=\"myPage.html\">myPage</a></body></html>");
		agent.addPageRequest("http://www1.test.com/myPage.html", newHtml("My Page", ""));
		
		Page page = agent.get("http://test.com");
		Link link = page.links().get(0);
		assertNotNull(link);
		Resource myPage = link.click();
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
		Resource myPage = link.click();
		assertEquals("My Page", myPage.getTitle());
	}

}