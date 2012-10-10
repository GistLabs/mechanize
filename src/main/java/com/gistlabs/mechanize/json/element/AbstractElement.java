package com.gistlabs.mechanize.json.element;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.gistlabs.mechanize.json.Element;
import com.gistlabs.mechanize.json.exceptions.JsonArrayException;

public abstract class AbstractElement implements Element {
	protected final String name;
	protected final Element parent;

	public AbstractElement(String name) {
		this(null, name);
	}
		
	public AbstractElement(Element parent, String name) {
		this.parent = parent;
		this.name = name;
	}
		
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public Element getParent() {
		return this.parent;
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

	protected Element factory(String key, Object obj) {
		if (obj instanceof JSONObject)
			return new ElementImpl(this, key, (JSONObject)obj);
		else if (obj instanceof JSONArray)
			throw new JsonArrayException("Can't access a single array entry without index", (JSONArray)obj);
		else 
			return new AttributeElement(this, key);
	}

	protected Element factory(String key, Object obj, JSONArray array, int index) {
		if (obj instanceof JSONObject)
			return new ElementImpl(this, key, (JSONObject)obj);
		else if (obj instanceof JSONArray)
			return new ArrayElementImpl(this, key, (JSONArray)obj);//throw new JsonArrayException("Can't access a single array entry without index", (JSONArray)obj);
		else 
			return new IndexedAttributeElement(this, key, array, index);
	}
}