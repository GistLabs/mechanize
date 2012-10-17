package com.gistlabs.mechanize.json.query;

import static org.junit.Assert.*;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.gistlabs.mechanize.json.node.JsonNode;
import com.gistlabs.mechanize.json.node.impl.JsonNodeHelper;
import com.gistlabs.mechanize.json.node.impl.ObjectNodeImpl;
import com.gistlabs.mechanize.util.css_query.NodeSelector;


public class PseudoNodeSelectorTest {

	protected NodeSelector<JsonNode> build(String json) throws JSONException {
		ObjectNodeImpl node = new ObjectNodeImpl(new JSONObject(json));
		NodeSelector<JsonNode> selector = new NodeSelector<JsonNode>(new JsonNodeHelper(node), node);
		return selector;
	}

	@Test
	public void testPseudoFirstOfType() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"a\" : 2 } ] }");
		
		List<JsonNode> result = selector.findAll("results:first-of-type");
		assertEquals(1, result.size());
		assertEquals("results", result.get(0).getName());
		assertEquals("1", result.get(0).getAttribute("a"));
	}	

	@Test
	public void testPseudoLastOfType() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"a\" : 2 } ] }");
		
		List<JsonNode> result = selector.findAll("results:last-of-type");
		assertEquals(1, result.size());
		assertEquals("results", result.get(0).getName());
		assertEquals("2", result.get(0).getAttribute("a"));
	}	

	@Test
	public void testPseudoNth() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }");
		
		List<JsonNode> result = selector.findAll("results:nth-child(1)");
		assertEquals(1, result.size());
		assertEquals("results", result.get(0).getName());
		assertEquals("1", result.get(0).getAttribute("a"));
		
		assertEquals("2", selector.find("results:nth-child(2)").getAttribute("b"));
	}	
}


