/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html;

import static com.gistlabs.mechanize.document.html.query.HtmlQueryBuilder.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.gistlabs.mechanize.MechanizeTestCase;
import com.gistlabs.mechanize.document.node.Node;
import com.gistlabs.mechanize.document.node.NodeVisitor;

/**
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public class HtmlNodeTest extends MechanizeTestCase {
	@Test
	public void testGettingTheParent() {
		agent.addPageRequest("http://test.com", 
				newHtml("Test Page", "<a href=\"link\">link</a>"));
		
		HtmlDocument page = agent.get("http://test.com");
		HtmlElement element = page.getRoot().get(byHRef(regEx("link")));
		assertEquals("body", element.getParent().getName());
		assertEquals("html", element.getParent().getParent().getName());
		assertNull(element.getParent().getParent().getParent().getParent());
	}

	@Test
	public void testVisitWithNodeVisitor() {
		agent.addPageRequest("http://test.com", 
				newHtml("Test Page", "<a href=\"link\">link</a>"));
		
		HtmlDocument page = agent.get("http://test.com");
		MyNodeVisitor visitor = new MyNodeVisitor();
		page.getRoot().visit(visitor);
		assertEquals("<#root<html<head<title<null>>><body<a>>>>", visitor.result.toString());
	}
	
	public static class MyNodeVisitor implements NodeVisitor {
		StringBuilder result = new StringBuilder();
		
		@Override
		public boolean beginNode(Node node) {
			result.append("<");
			result.append(node.getName());
			return node.getName() != null && !node.getName().equals("a");
		}
		
		@Override
		public void endNode(Node node) {
			result.append(">");
		}
	}
}
