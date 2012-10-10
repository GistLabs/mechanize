package com.gistlabs.mechanize.json.element;

import org.json.JSONArray;
import org.json.JSONException;

import com.gistlabs.mechanize.json.Element;
import com.gistlabs.mechanize.json.exceptions.JsonException;

public class IndexedAttributeElement extends AttributeElement {
	
	private final int index;
	private JSONArray array;

	public IndexedAttributeElement(Element parent, String name, JSONArray array, int index) {
		super(parent, name);
		this.array = array;
		this.index = index;
	}

	@Override
	public String getContent() {
		try {
			return this.array.getString(this.index);
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

	@Override
	public void setContent(String value) {
		try {
			this.array.put(this.index, value);
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}
}
