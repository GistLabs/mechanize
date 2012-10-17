package com.gistlabs.mechanize.json.query;

import static org.junit.Assert.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.gistlabs.mechanize.json.node.JsonNode;
import com.gistlabs.mechanize.json.node.impl.ObjectNodeImpl;


public class ElementQueryTest {

	protected JsonNode build(String json) throws JSONException {
		return new ObjectNodeImpl(new JSONObject(json));
	}
	
	@Test
	public void testAttributeTilda() throws Exception {
		JsonNode node = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y foo bar\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }");
		
		assertNotNull(node.find("b[x~=\"y\"]"));
		assertEquals(1, node.findAll("b[x~=\"foo\"]").size());
		assertEquals(1, node.findAll("b[x~=\"bar\"]").size());

		assertEquals(0, node.findAll("b[x~=\"baz\"]").size());
	}
}


