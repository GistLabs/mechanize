package com.gistlabs.mechanize.json.element;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.gistlabs.mechanize.json.Element;
import com.gistlabs.mechanize.json.JsonException;

public class ElementImpl extends AbstractElement implements Element {

	private JSONObject obj;
	private Map<String,AttributeElement> attributes = new HashMap<String, AttributeElement>();

	public ElementImpl(JSONObject obj) {
		this("root", obj);
		
	}
	
	public ElementImpl(String name, JSONObject obj) {
		super(name);
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
	public Collection<String> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContent(final String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Element getChild(final String key) {
		if (!attributes.containsKey(key))
			attributes.put(key, new AttributeElement(this, key));
		
		return attributes.get(key);
	}
	
	@Override
	public List<Element> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element find(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Element> findAll(String query) {
		// TODO Auto-generated method stub
		return null;
	}

}
