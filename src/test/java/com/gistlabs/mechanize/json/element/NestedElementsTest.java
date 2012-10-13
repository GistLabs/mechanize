package com.gistlabs.mechanize.json.element;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gistlabs.mechanize.json.JsonNode;
import com.gistlabs.mechanize.json.nodeImpl.ObjectNodeImpl;

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
