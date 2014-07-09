package com.gistlabs.mechanize.document.json.node;

import com.gistlabs.mechanize.document.json.node.impl.ObjectNodeImpl;
import com.gistlabs.mechanize.document.json.node.impl.TestElementBaseClass;

public abstract class JsonNodeTestCase extends TestElementBaseClass {

	public JsonNode from(String json) {
		return new ObjectNodeImpl(parseJson(json));
	}
			
}
