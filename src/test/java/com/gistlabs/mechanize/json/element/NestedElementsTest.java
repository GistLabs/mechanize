package com.gistlabs.mechanize.json.element;

import static org.junit.Assert.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.gistlabs.mechanize.json.JsonException;
import com.gistlabs.mechanize.json.element.ElementImpl;

public class NestedElementsTest {

	@Test
	public void testString() {
		JSONObject parsedJson = parseJson("{ \"one\" : \"two\", \"\" : \"four\", \"a:b\" : \"http://example.com\" }");
		ElementImpl element = new ElementImpl(parsedJson);
		
		assertEquals("two", element.getAttribute("one"));
		assertEquals("four", element.getAttribute(""));
		assertEquals("http://example.com", element.getAttribute("a:b"));
	}
	
	@Test
	public void testStringCasting() {
		ElementImpl element = new ElementImpl(parseJson("{ \"one\" : \"two\", \"\" : \"four\", \"a:b\" : \"http://example.com\" }"));
		
		assertEquals("four", element.getAttribute(""));
		assertEquals("two", element.getChild("one").getContent());
		assertEquals("four", element.getChild("").getContent());
	}

	@Test
	public void testNumber() {
		ElementImpl element = new ElementImpl(parseJson("{ \"one\" : 2, \"b\" : 2.2 }"));
		
		assertEquals("2", element.getAttribute("one"));
		assertEquals("2.2", element.getChild("b").getContent());
	}
	
	@Test
	public void testParentage() {
		ElementImpl element = new ElementImpl(parseJson("{ \"one\" : 2, \"b\" : 2.2 }"));

		assertNull(element.getParent());
		assertEquals(element, element.getChild("one").getParent());	
	}
	
	@Test
	public void testSetAttributes() {
		ElementImpl element = new ElementImpl(parseJson("{ \"one\" : 2, \"b\" : 2.2 }"));

		assertEquals("2", element.getAttribute("one"));
		element.setAttribute("one", "new");
		assertEquals("new", element.getAttribute("one"));
		
		assertEquals("2.2", element.getChild("b").getContent());
		element.getChild("b").setContent("maybe");
		assertEquals("maybe", element.getChild("b").getContent());
	}
	
	@Test
	public void confirmNullHandling() {
		ElementImpl element = new ElementImpl(parseJson("{ \"one\" : 2, \"b\" : null }"));
		
		assertNull(element.getAttribute("b"));
		assertTrue(element.hasAttribute("b"));
		
		element.setAttribute("one", null);
		assertNull(element.getAttribute("one"));
		assertTrue(element.hasAttribute("one"));	
	}
	
	@Test
	public void testStableChildren() {
		ElementImpl element = new ElementImpl(parseJson("{ \"one\" : 2, \"b\" : 2.2 }"));
		
		assertEquals(element.getChild("b"), element.getChild("b"));		
	}
	
	protected JSONObject parseJson(String json) {
		try {
			return new JSONObject(json);
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

}
