package com.gistlabs.mechanize.json.impl;

import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.gistlabs.mechanize.json.Element;
import com.gistlabs.mechanize.json.JsonException;

public class ElementImpl implements Element {
	private final String name;
	private final JSONObject obj;

	public ElementImpl(JSONObject obj) {
		this("root", obj);
	}
	
	public ElementImpl(String name, JSONObject obj) {
		this.name = name;
		this.obj = obj;
	}
		
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getAttribute(String key) {
		try {
			return (String) obj.get(key);
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

	@Override
	public void setAttribute(String key, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasAttribute(String key) {
		// TODO Auto-generated method stub
		return false;
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
	public void setContent(String value) {
		// TODO Auto-generated method stub
		
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
