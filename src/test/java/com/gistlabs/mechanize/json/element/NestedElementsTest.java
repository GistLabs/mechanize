package com.gistlabs.mechanize.json.element;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gistlabs.mechanize.json.Element;
import com.gistlabs.mechanize.json.element.ElementImpl;

public class NestedElementsTest extends TestElementBaseClass {
	
	@Test
	public void testNestedElement() {
		ElementImpl element = new ElementImpl(parseJson("{ \"one\" : 2, \"b\" : { \"a\" : \"x\", \"c\" : 4 } }"));
		
		Element nested = element.getChild("b");
		assertNotNull(nested);
		assertTrue(nested instanceof ElementImpl);
		assertEquals("b", nested.getName());
		assertEquals(element, nested.getParent());
		assertEquals("x", nested.getAttribute("a"));
	}

}
