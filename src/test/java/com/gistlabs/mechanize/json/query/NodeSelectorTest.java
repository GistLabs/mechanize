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


public class NodeSelectorTest {

	protected NodeSelector<JsonNode> build(String json) throws JSONException {
		ObjectNodeImpl node = new ObjectNodeImpl(new JSONObject(json));
		NodeSelector<JsonNode> selector = new NodeSelector<JsonNode>(new JsonNodeHelper(node), node);
		return selector;
	}
	
	@Test
	public void testStar() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }");
		
		List<JsonNode> result = selector.findAll("*");
		assertEquals(4, result.size());
	}
	
	@Test
	public void testName() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }");
		
		List<JsonNode> result = selector.findAll("b");
		assertEquals(1, result.size());
	}

	@Test
	public void testAttributePresent() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }");
		
		List<JsonNode> result = selector.findAll("b[x]");
		assertEquals(1, result.size());
		assertEquals("y", result.get(0).getAttribute("x"));
		
		assertTrue(selector.findAll("b[z]").isEmpty());
	}

	@Test
	public void testAttributeEquals() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }");
		
		List<JsonNode> result = selector.findAll("b[x=\"y\"]");
		assertEquals(1, result.size());
		assertEquals("y", result.get(0).getAttribute("x"));
		
		assertTrue(selector.findAll("b[x=\"z\"]").isEmpty());
	}

	@Test
	public void testAttributeTilda() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y foo bar\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }");
		
		assertEquals(1, selector.findAll("b[x~=\"y\"]").size());
		assertEquals(1, selector.findAll("b[x~=\"foo\"]").size());
		assertEquals(1, selector.findAll("b[x~=\"bar\"]").size());

		assertEquals(0, selector.findAll("b[x~=\"baz\"]").size());
	}

	@Test
	public void testWildcardWithAttribute() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }");
		
		List<JsonNode> result = selector.findAll("*[x]");
		assertEquals(1, result.size());
		assertEquals("y", result.get(0).getAttribute("x"));
		
		assertTrue(selector.findAll("b[z]").isEmpty());
	}
	@Test
	public void testNot() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"a\" : 2 } ] }");
		
		List<JsonNode> result = selector.findAll("*:not([x])");
		assertEquals(3, result.size());
		assertEquals("root", result.get(0).getName());
		assertEquals("2", result.get(0).getAttribute("a"));
		
		List<JsonNode> result2 = selector.findAll("*:not([a])");
		assertEquals(1, result2.size());
		assertEquals("b", result2.get(0).getName());
	}	
}


