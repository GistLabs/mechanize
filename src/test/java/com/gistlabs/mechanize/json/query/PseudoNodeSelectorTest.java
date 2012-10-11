package com.gistlabs.mechanize.json.query;

import static org.junit.Assert.*;

import java.util.List;

import org.json.JSONObject;
import org.junit.Test;

import com.gistlabs.mechanize.json.Node;
import com.gistlabs.mechanize.json.nodeImpl.ObjectNodeImpl;

public class PseudoNodeSelectorTest {

	@Test
	public void testPseudoFirstOfType() throws Exception {
		ObjectNodeImpl node = new ObjectNodeImpl(new JSONObject("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"a\" : 2 } ] }"));
		NodeSelector selector = new NodeSelector(node);
		
		List<Node> result = selector.findAll("results:first-of-type");
		assertEquals(1, result.size());
		assertEquals("results", result.get(0).getName());
		assertEquals("1", result.get(0).getAttribute("a"));
	}	

	@Test
	public void testPseudoLastOfType() throws Exception {
		ObjectNodeImpl node = new ObjectNodeImpl(new JSONObject("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"a\" : 2 } ] }"));
		NodeSelector selector = new NodeSelector(node);
		
		List<Node> result = selector.findAll("results:last-of-type");
		assertEquals(1, result.size());
		assertEquals("results", result.get(0).getName());
		assertEquals("2", result.get(0).getAttribute("a"));
	}	
}


