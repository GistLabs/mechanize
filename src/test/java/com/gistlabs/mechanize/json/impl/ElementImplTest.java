package com.gistlabs.mechanize.json.impl;

import static org.junit.Assert.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.gistlabs.mechanize.json.JsonException;

public class ElementImplTest {

	@Test
	public void testString() {
		ElementImpl element = new ElementImpl(parseJson("{ \"one\" : \"two\", \"\" : \"four\", \"a:b\" : \"http://example.com\" }"));
		
		assertEquals("two", element.getAttribute("one"));
		assertEquals("four", element.getAttribute(""));
		assertEquals("http://example.com", element.getAttribute("a:b"));
	}
	
	@Test
	public void testStringCasting() {
		ElementImpl element = new ElementImpl(parseJson("{ \"one\" : \"two\", \"\" : \"four\", \"a:b\" : \"http://example.com\" }"));
		
		assertEquals("four", element.getAttribute(""));
		assertEquals("http://example.com", element.getAttribute("a:b"));
	}

	protected JSONObject parseJson(String json) {
		try {
			return new JSONObject(json);
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}
	
	@Test
	public void testNumber() {
		ElementImpl element = new ElementImpl(parseJson("{ \"one\" : \"two\", \"\" : \"four\", \"a:b\" : \"http://example.com\" }"));
		
	}

}
