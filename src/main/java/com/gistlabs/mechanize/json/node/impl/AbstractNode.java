package com.gistlabs.mechanize.json.node.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gistlabs.mechanize.json.exceptions.JsonArrayException;
import com.gistlabs.mechanize.json.exceptions.JsonException;
import com.gistlabs.mechanize.json.node.JsonNode;
import com.gistlabs.mechanize.util.css_query.NodeSelector;

public abstract class AbstractNode extends com.gistlabs.mechanize.document.node.AbstractNode implements JsonNode {
	protected final String name;
	protected final JsonNode parent;
		
	public AbstractNode(JsonNode parent, String name) {
		this.parent = parent;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return String.format("%s(%s)", getName(), join(getAttributeNames(),","));
	}
	
	static String join(Collection<?> s, String delimiter) {
	     StringBuilder builder = new StringBuilder();
	     Iterator<?> iter = s.iterator();
	     while (iter.hasNext()) {
	         builder.append(iter.next());
	         if (!iter.hasNext()) {
	           break;                  
	         }
	         builder.append(delimiter);
	     }
	     return builder.toString();
	 }
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public JsonNode getParent() {
		return this.parent;
	}

	@Override
	public List<? extends JsonNode> getChildren() {
		return Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public NodeSelector<JsonNode> buildNodeSelector() {
		return (NodeSelector<JsonNode>) super.buildNodeSelector();
	}
	protected JsonNode factory(JSONObject node, String key) {		
		try {
			if (!node.has(key))
				return null;

			Object obj = node.get(key);
			
			if (obj instanceof JSONObject)
				return new ObjectNodeImpl(this, key, (JSONObject)obj);
			else if (obj instanceof JSONArray)
				throw new JsonArrayException("Can't access a single array entry without index", (JSONArray)obj);
			else 
				return new AttributeNode(this, key);
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

	protected JsonNode factory(String key, Object obj, JSONArray array, int index) {
		if (obj instanceof JSONObject)
			return new ObjectNodeImpl(this, key, (JSONObject)obj);
		else if (obj instanceof JSONArray)
			return new ArrayNodeImpl(this, key, (JSONArray)obj);//throw new JsonArrayException("Can't access a single array entry without index", (JSONArray)obj);
		else 
			return new IndexedAttributeNode(this, key, array, index);
	}
}