package com.gistlabs.mechanize.json.element;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.gistlabs.mechanize.json.Element;
import com.gistlabs.mechanize.json.JsonArrayException;
import com.gistlabs.mechanize.json.element.ElementImpl;

public class ArrayElementsTest extends TestElementBaseClass {
	
	@Test
	public void testArrayElement() {
		ElementImpl element = new ElementImpl(parseJson("{ \"one\" : 2, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }"));
		
		List<Element> array = element.getChildren("results");
		assertNotNull(array);
		assertEquals("results", array.get(0).getName());
		assertEquals(element, array.get(0).getParent());
		assertEquals("1", array.get(0).getAttribute("a"));
		assertEquals("2", array.get(1).getAttribute("b"));
	}
	
	@Test(expected=JsonArrayException.class)
	public void testChildFails() {
		ElementImpl element = new ElementImpl(parseJson("{ \"one\" : 2, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }"));
		
		element.getChild("results");
	}

}
