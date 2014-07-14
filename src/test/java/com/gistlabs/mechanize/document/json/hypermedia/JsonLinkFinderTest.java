/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.hypermedia;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.gistlabs.mechanize.document.json.hypermedia.JsonLink;
import com.gistlabs.mechanize.document.json.hypermedia.JsonLinkFinder;
import com.gistlabs.mechanize.document.json.node.JsonNode;
import com.gistlabs.mechanize.document.json.node.JsonNodeTestCase;
import com.gistlabs.mechanize.util.Util;

public class JsonLinkFinderTest extends JsonNodeTestCase {
	JsonNode json;
	JsonLinkFinder finder = new JsonLinkFinder();
	
	@Before
	public void parseJson() {
		String jsonString = Util.getStringFromInputStream(getClass().getResourceAsStream("find-links.json"));
		json = from(jsonString);
	}
	
	@Test
	public void testFindingOneHref() {
		JsonNode one = json.find("one");
		List<JsonLink> links = finder.findOn(one);
		assertEquals(1, links.size());
		
		JsonLink hrefLink = links.get(0);
		assertEquals(hrefLink.node(), one);
	}
	
	@Test
	public void testFindingTwoLinks() {
		JsonNode two = json.find("two");
		List<JsonLink> links = finder.findOn(two);
		assertEquals(2, links.size());
		
		// collect data from the json
		Map<String, String> data = new HashMap<String, String>();
		for (String name : two.getAttributeNames()) {
			data.put(name, two.getAttribute(name));
		}
		
		// make sure the values all match
		for (JsonLink link : links) {
			assertEquals(data.get(link.attrName()), link.uri());
		}
	}
	
	@Test
	public void testFindingThreeLinks() {
		JsonNode three = json.find("three");
		List<JsonLink> links = finder.findOn(three);
		assertEquals(3, links.size());
	}
	
	@Test
	public void testLinkRelIsName() {
		JsonNode one = json.find("rel-is-name");
		List<JsonLink> links = finder.findOn(one);
		assertEquals(1, links.size());
		
		JsonLink hrefLink = links.get(0);
		assertEquals("rel-is-name", hrefLink.linkRel());
	}
	
	@Test
	public void testLinkRelIsRel() {
		JsonNode one = json.find("rel-is-rel");
		List<JsonLink> links = finder.findOn(one);
		assertEquals(1, links.size());
		
		JsonLink hrefLink = links.get(0);
		assertEquals("self", hrefLink.linkRel());
	}
	
	@Test
	public void testLinkRelIsPrefix() {
		JsonNode one = json.find("rel-is-prefix");
		List<JsonLink> links = finder.findOn(one);
		assertEquals(1, links.size());
		
		JsonLink hrefLink = links.get(0);
		assertEquals("prefix", hrefLink.linkRel());
	}
	
	@Test
	public void testNestedLinksDepthOne() {
		JsonNode node = json.find("nested-links");
		List<JsonLink> links = finder.findOn(node);
		assertEquals(1, links.size());
		
		JsonLink hrefLink = links.get(0);
		assertEquals("self", hrefLink.linkRel());
	}
	
	@Test
	public void testNestedLinksDepthChildren() {
		JsonNode node = json.find("nested-links");
		List<JsonLink> links = finder.findWithChildren(node);
		assertEquals(3, links.size());
		
		JsonLink next = links.get(1); // this index is dependent on ordering of children
		assertEquals("next", next.linkRel());
	}
	
	@Test
	public void testNestedLinksRecursive() {
		JsonNode node = json.find("nested-links");
		List<JsonLink> links = finder.findRecursive(node);
		assertEquals(5, links.size());
	}

}
