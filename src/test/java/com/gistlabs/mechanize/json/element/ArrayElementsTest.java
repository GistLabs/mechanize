package com.gistlabs.mechanize.json.element;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.gistlabs.mechanize.json.Element;
import com.gistlabs.mechanize.json.element.ElementImpl;
import com.gistlabs.mechanize.json.exceptions.JsonArrayException;

public class ArrayElementsTest extends TestElementBaseClass {
	
	@Test
	public void testArrayObjects() {
		ElementImpl element = new ElementImpl(parseJson("{ \"one\" : 2, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }"));
		
		List<Element> array = element.getChildren("results");
		assertNotNull(array);
		assertEquals("results", array.get(0).getName());
		assertEquals(element, array.get(0).getParent());
		assertEquals("1", array.get(0).getAttribute("a"));
		assertEquals("2", array.get(1).getAttribute("b"));
	}
	
	@Test
	public void testArrayPrimitives() {
		ElementImpl element = new ElementImpl(parseJson("{ \"results\" : [ 1,2] }"));
		
		List<Element> array = element.getChildren("results");
		assertNotNull(array);
		assertEquals(2, array.size());
		assertEquals("results", array.get(0).getName());
		assertEquals("1", array.get(0).getContent());
		
		array.get(0).setContent("4");
		assertEquals("results", array.get(0).getName());
		assertEquals("4", array.get(0).getContent());
	}
	
	@Test
	public void testNestedArrays() {
		ElementImpl element = new ElementImpl(parseJson("{ \"results\" : [ [1,2], [3,4] ] }"));
		
		List<Element> array = element.getChildren("results");
		assertNotNull(array);
		assertEquals(2, array.size());
		assertEquals("results", array.get(0).getName());
		
	}
	
	@Test(expected=JsonArrayException.class)
	public void testChildFails() {
		ElementImpl element = new ElementImpl(parseJson("{ \"one\" : 2, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }"));
		
		element.getChild("results");
	}

}
