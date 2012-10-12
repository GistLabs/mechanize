package com.gistlabs.mechanize.json.nodeImpl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gistlabs.mechanize.json.Node;
import com.gistlabs.mechanize.json.exceptions.JsonArrayException;
import com.gistlabs.mechanize.json.exceptions.JsonException;
import com.gistlabs.mechanize.json.query.NodeSelector;

public abstract class AbstractNode implements Node {
	protected final String name;
	protected final Node parent;
		
	public AbstractNode(Node parent, String name) {
		this.parent = parent;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return String.format("%s(%s)", getName(), join(getAttributes(),","));
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
	public Node getParent() {
		return this.parent;
	}

	@Override
	public Node find(String query) {
		return new NodeSelector<Node>(new JsonNodeHelper(), this).find(query);
	}

	@Override
	public List<Node> findAll(String query) {
		return new NodeSelector<Node>(new JsonNodeHelper(), this).findAll(query);
	}

	protected Node factory(JSONObject node, String key) {		
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

	protected Node factory(String key, Object obj, JSONArray array, int index) {
		if (obj instanceof JSONObject)
			return new ObjectNodeImpl(this, key, (JSONObject)obj);
		else if (obj instanceof JSONArray)
			return new ArrayNodeImpl(this, key, (JSONArray)obj);//throw new JsonArrayException("Can't access a single array entry without index", (JSONArray)obj);
		else 
			return new IndexedAttributeNode(this, key, array, index);
	}
}