package com.gistlabs.mechanize.json.element;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;

import com.gistlabs.mechanize.json.node.impl.ObjectNodeImpl;

public class ElementAttributesTest extends TestElementBaseClass {

	@Test
	public void testString() {
		JSONObject parsedJson = parseJson("{ \"one\" : \"two\", \"\" : \"four\", \"a:b\" : \"http://example.com\" }");
		ObjectNodeImpl element = new ObjectNodeImpl(parsedJson);
		
		assertEquals("two", element.getAttribute("one"));
		assertEquals("four", element.getAttribute(""));
		assertEquals("http://example.com", element.getAttribute("a:b"));
	}
	
	@Test
	public void testStringCasting() {
		ObjectNodeImpl element = new ObjectNodeImpl(parseJson("{ \"one\" : \"two\", \"\" : \"four\", \"a:b\" : \"http://example.com\" }"));
		
		assertEquals("four", element.getAttribute(""));
		assertEquals("two", element.getChild("one").getValue());
		assertEquals("four", element.getChild("").getValue());
	}

	@Test
	public void testNumber() {
		ObjectNodeImpl element = new ObjectNodeImpl(parseJson("{ \"one\" : 2, \"b\" : 2.2 }"));
		
		assertEquals("2", element.getAttribute("one"));
		assertEquals("2.2", element.getChild("b").getValue());
	}
	
	@Test
	public void testParentage() {
		ObjectNodeImpl element = new ObjectNodeImpl(parseJson("{ \"one\" : 2, \"b\" : 2.2 }"));

		assertNull(element.getParent());
		assertEquals(element, element.getChild("one").getParent());	
	}
	
	@Test
	public void testSetAttributes() {
		ObjectNodeImpl element = new ObjectNodeImpl(parseJson("{ \"one\" : 2, \"b\" : 2.2 }"));

		assertEquals("2", element.getAttribute("one"));
		element.setAttribute("one", "new");
		assertEquals("new", element.getAttribute("one"));
		
		assertEquals("2.2", element.getChild("b").getValue());
		element.getChild("b").setValue("maybe");
		assertEquals("maybe", element.getChild("b").getValue());
	}
	
	@Test
	public void confirmNullHandling() {
		ObjectNodeImpl element = new ObjectNodeImpl(parseJson("{ \"one\" : 2, \"b\" : null }"));
		
		assertNull(element.getAttribute("b"));
		assertTrue(element.hasAttribute("b"));
		
		element.setAttribute("one", null);
		assertNull(element.getAttribute("one"));
		assertTrue(element.hasAttribute("one"));	
	}
	
	@Test
	public void testStableChildren() {
		ObjectNodeImpl element = new ObjectNodeImpl(parseJson("{ \"one\" : 2, \"b\" : 2.2 }"));
		
		assertEquals(element.getChild("b"), element.getChild("b"));		
	}
}
