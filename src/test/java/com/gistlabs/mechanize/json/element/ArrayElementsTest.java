package com.gistlabs.mechanize.json.element;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.gistlabs.mechanize.json.JsonNode;
import com.gistlabs.mechanize.json.exceptions.JsonException;
import com.gistlabs.mechanize.json.nodeImpl.ObjectNodeImpl;

public class ArrayElementsTest extends TestElementBaseClass {
	
	@Test
	public void testArrayObjects() {
		ObjectNodeImpl element = new ObjectNodeImpl(parseJson("{ \"one\" : 2, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }"));
		
		List<JsonNode> array = element.getChildren("results");
		assertNotNull(array);
		assertEquals("results", array.get(0).getName());
		assertEquals(element, array.get(0).getParent());
		assertEquals("1", array.get(0).getAttribute("a"));
		assertEquals("2", array.get(1).getAttribute("b"));
	}
	
	@Test
	public void testArrayPrimitives() {
		ObjectNodeImpl element = new ObjectNodeImpl(parseJson("{ \"results\" : [ 1,2] }"));
		
		List<JsonNode> array = element.getChildren("results");
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
		ObjectNodeImpl element = new ObjectNodeImpl(parseJson("{ \"results\" : [ [1,2], [3,4] ] }"));
		
		List<JsonNode> array = element.getChildren("results");
		assertNotNull(array);
		assertEquals(2, array.size());
		assertEquals("results", array.get(0).getName());
		
	}
	
	@Test(expected=JsonException.class)
	public void testChildFails() {
		ObjectNodeImpl element = new ObjectNodeImpl(parseJson("{ \"one\" : 2, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }"));
		
		element.getChild("results");
	}
	
	@Test
	public void testGetChildrenStar() {
		ObjectNodeImpl element = new ObjectNodeImpl(parseJson("{ \"one\" : [2,3], \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 },  ] }"));
		
		assertEquals(2, element.getChildren("results").size());
		assertEquals(4, element.getChildren("*").size());
	}

}
