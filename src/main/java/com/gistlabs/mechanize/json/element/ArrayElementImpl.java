package com.gistlabs.mechanize.json.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.gistlabs.mechanize.json.Element;
import com.gistlabs.mechanize.json.exceptions.JsonException;

/**
 * Support for nested arrays
 */
public class ArrayElementImpl extends AbstractElement implements Element {
	private final JSONArray array;
	private List<Element> children;


	public ArrayElementImpl(Element parent, String key, JSONArray array) {
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
	public Element getChild(final String key) {
		return null;
	}

	@Override
	public List<Element> getChildren() {
		try {
			if (children==null) {
				children = new ArrayList<Element>();
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
	public List<Element> getChildren(String key) {
		return Collections.EMPTY_LIST;
	}

}
