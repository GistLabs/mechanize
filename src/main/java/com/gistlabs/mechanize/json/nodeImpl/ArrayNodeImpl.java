package com.gistlabs.mechanize.json.nodeImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.gistlabs.mechanize.json.JsonNode;
import com.gistlabs.mechanize.json.exceptions.JsonException;

/**
 * Support for nested arrays
 */
public class ArrayNodeImpl extends AbstractNode {
	private final JSONArray array;
	private List<JsonNode> children;


	public ArrayNodeImpl(JsonNode parent, String key, JSONArray array) {
		super(parent, key);
		this.array = array;
	}

	@Override
	public String getAttribute(String key) {
		return null;
	}

	@Override
	public void setAttribute(final String key, final String value) {
	}

	@Override
	public boolean hasAttribute(String key) {
		return false;
	}

	@Override
	public Collection<String> getAttributes() {
		return null;
	}

	@Override
	public String getContent() {
		return null;
	}

	@Override
	public void setContent(final String value) {
	}

	@Override
	public JsonNode getChild(final String key) {
		return null;
	}

	@Override
	public List<JsonNode> getChildren() {
		try {
			if (children==null) {
				children = new ArrayList<JsonNode>();
				for(int i=0;i < array.length();i++){
					Object obj = array.get(i);
					children.add(factory("array", obj, array, i));
				}
			}
			return children;
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JsonNode> getChildren(String key) {
		return Collections.EMPTY_LIST;
	}

}
