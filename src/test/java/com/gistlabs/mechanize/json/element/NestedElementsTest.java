package com.gistlabs.mechanize.json.element;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gistlabs.mechanize.json.Node;
import com.gistlabs.mechanize.json.nodeImpl.ObjectNodeImpl;

public class NestedElementsTest extends TestElementBaseClass {
	
	@Test
	public void testNestedElement() {
		ObjectNodeImpl element = new ObjectNodeImpl(parseJson("{ \"one\" : 2, \"b\" : { \"a\" : \"x\", \"c\" : 4 } }"));
		
		Node nested = element.getChild("b");
		assertNotNull(nested);
		assertTrue(nested instanceof ObjectNodeImpl);
		assertEquals("b", nested.getName());
		assertEquals(element, nested.getParent());
		assertEquals("x", nested.getAttribute("a"));
	}

}
