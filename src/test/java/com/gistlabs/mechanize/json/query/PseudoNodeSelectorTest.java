package com.gistlabs.mechanize.json.query;

import static org.junit.Assert.*;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.gistlabs.mechanize.json.Node;
import com.gistlabs.mechanize.json.nodeImpl.JsonNodeHelper;
import com.gistlabs.mechanize.json.nodeImpl.ObjectNodeImpl;
import com.gistlabs.mechanize.json.query.NodeSelector;


public class PseudoNodeSelectorTest {

	protected NodeSelector<Node> build(String json) throws JSONException {
		ObjectNodeImpl node = new ObjectNodeImpl(new JSONObject(json));
		NodeSelector<Node> selector = new NodeSelector<Node>(new JsonNodeHelper(), node);
		return selector;
	}

	@Test
	public void testPseudoFirstOfType() throws Exception {
		NodeSelector<Node> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"a\" : 2 } ] }");
		
		List<Node> result = selector.findAll("results:first-of-type");
		assertEquals(1, result.size());
		assertEquals("results", result.get(0).getName());
		assertEquals("1", result.get(0).getAttribute("a"));
	}	

	@Test
	public void testPseudoLastOfType() throws Exception {
		NodeSelector<Node> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"a\" : 2 } ] }");
		
		List<Node> result = selector.findAll("results:last-of-type");
		assertEquals(1, result.size());
		assertEquals("results", result.get(0).getName());
		assertEquals("2", result.get(0).getAttribute("a"));
	}	
}


