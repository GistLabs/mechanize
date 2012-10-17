package com.gistlabs.mechanize.json.node.impl;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gistlabs.mechanize.json.exceptions.JsonArrayException;
import com.gistlabs.mechanize.json.exceptions.JsonException;
import com.gistlabs.mechanize.json.node.JsonNode;

public class ObjectNodeImpl extends AbstractNode {

	private final JSONObject obj;
	private Map<String,List<JsonNode>> children = new HashMap<String, List<JsonNode>>();

	public ObjectNodeImpl(JSONObject obj) {
		this("root", obj);
		
	}
	
	public ObjectNodeImpl(String name, JSONObject obj) {
		this(null, name, obj);
	}

	public ObjectNodeImpl(JsonNode parent, String name, JSONObject obj) {
		super(parent, name);
		this.obj = obj;
	}

	@Override
	public String getAttribute(String key) {
		try {
			Object value = obj.get(key);
			if (value==JSONObject.NULL)
				return null;
			
			return value.toString();
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

	@Override
	public void setAttribute(final String key, final String value) {
		try {
			obj.put(key, value==null ? JSONObject.NULL : value);
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

	@Override
	public boolean hasAttribute(String key) {
		return obj.has(key);
	}

	@Override
	public List<String> getAttributeNames() {
		try {
			List<String> result = new ArrayList<String>();
			@SuppressWarnings("unchecked")
			Iterator<String> keys = this.obj.keys();
			while(keys.hasNext()) {
				String key = keys.next();
				if (isPrimitive(this.obj.get(key)))
					result.add(key);
			}
			return result;
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

	@Override
	public String getValue() {
		return null;
	}

	@Override
	public void setValue(final String value) {
		throw new JsonException("JSON Objects can't directly have coSntent");
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonNode getChild(final String key) {
		List<JsonNode> result = getChildren(key);
		
		if (result.size()>=2)
			throw new JsonException("More than one result");
		else if (result.isEmpty())
			return null;
		else
			return result.get(0);
	}
	
	protected boolean isPrimitive(Object jsonObject) {
		return !(jsonObject instanceof JSONObject || jsonObject instanceof JSONArray);
	}

	public List<JsonNode> getChildren() {
		try {
			List<JsonNode> result = new ArrayList<JsonNode>();
			@SuppressWarnings("unchecked")
			Iterator<String> keys = this.obj.keys();
			while(keys.hasNext()) {
				String key = keys.next();
				if (!isPrimitive(this.obj.get(key)))
					result.addAll(getChildren(key));
			}
			return result;
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

	@Override
	public List<JsonNode> getChildren(String... names) {
		int length = names.length;
		if (length==0)
			return getChildren();
		
		if (length==1) {
			if ("*".equalsIgnoreCase(names[0]))
				return getChildren();
			return lookupChildren(names[0]);
		}
		
		List<String> namesColl = Arrays.asList(names);
		List<JsonNode> result = new ArrayList<JsonNode>();
		for(JsonNode node : getChildren()) {
			if (namesColl.contains(node.getName()))
				result.add(node);
		}
		return result;
	}
	
	protected List<JsonNode> lookupChildren(String key) {
		if (!children.containsKey(key)) {
			if ("*".equalsIgnoreCase(key))
				children.put(key, getChildren());
			else
				children.put(key, factory(key));
		}
		
		return children.get(key);
	}

	protected List<JsonNode> factory(String key) {
		try {
			ArrayList<JsonNode> result = new ArrayList<JsonNode>();
			try {
				JsonNode n = factory(this.obj, key);
				if (n!=null)
					result.add(n);
			} catch(JsonArrayException e) {
				if (e.getArray()==null)
					throw e;
				
				JSONArray array = e.getArray();
				for(int i=0;i < array.length();i++){
					Object obj = array.get(i);
					result.add(factory(key, obj, array, i));
				}
			}
			return result;
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

}
