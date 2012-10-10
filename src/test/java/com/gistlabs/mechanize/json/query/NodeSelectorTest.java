package com.gistlabs.mechanize.json.query;

import static org.junit.Assert.*;

import java.util.List;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.gistlabs.mechanize.json.Node;
import com.gistlabs.mechanize.json.nodeImpl.ObjectNodeImpl;

public class NodeSelectorTest {
	
	@Test
	public void testStar() throws Exception {
		ObjectNodeImpl node = new ObjectNodeImpl(new JSONObject("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }"));
		NodeSelector selector = new NodeSelector(node);
		
		List<Node> result = selector.findAll("*");
		assertEquals(4, result.size());
	}
	
	@Test
	public void testName() throws Exception {
		ObjectNodeImpl node = new ObjectNodeImpl(new JSONObject("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }"));
		NodeSelector selector = new NodeSelector(node);
		
		List<Node> result = selector.findAll("b");
		assertEquals(1, result.size());
	}

}


