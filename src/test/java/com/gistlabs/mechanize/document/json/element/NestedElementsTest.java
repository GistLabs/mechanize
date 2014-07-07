/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.element;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gistlabs.mechanize.document.json.node.JsonNode;
import com.gistlabs.mechanize.document.json.node.impl.ObjectNodeImpl;

public class NestedElementsTest extends TestElementBaseClass {

	@Test
	public void testNestedElement() {
		ObjectNodeImpl element = new ObjectNodeImpl(parseJson("{ \"one\" : 2, \"b\" : { \"a\" : \"x\", \"c\" : 4 } }"));

		JsonNode nested = element.getChild("b");
		assertNotNull(nested);
		assertTrue(nested instanceof ObjectNodeImpl);
		assertEquals("b", nested.getName());
		assertEquals(element, nested.getParent());
		assertEquals("x", nested.getAttribute("a"));
	}

	@Test
	public void testNullChild() {
		ObjectNodeImpl element = new ObjectNodeImpl(parseJson("{ \"one\" : 2, \"b\" : { \"a\" : \"x\", \"c\" : 4 } }"));

		assertNull(element.getChild("c"));
	}

}
