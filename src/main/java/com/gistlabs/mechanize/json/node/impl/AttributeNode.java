package com.gistlabs.mechanize.json.node.impl;

import java.util.Collections;
import java.util.List;

import com.gistlabs.mechanize.json.node.JsonNode;

public class AttributeNode extends AbstractNode {
	
	public AttributeNode(JsonNode parent, String name) {
		super(parent, name);
	}

	@Override
	public String getAttribute(String key) {
		return null; 
	}

	@Override
	public void setAttribute(String key, String value) {
	}

	@Override
	public boolean hasAttribute(String key) {
		return false;
	}

	@Override
	public List<String> getAttributeNames() {
		return null;
	}

	@Override
	public String getValue() {
		return this.parent.getAttribute(getName());
	}

	@Override
	public void setValue(String value) {
		this.parent.setAttribute(getName(), value);
	}

	@Override
	public <T extends JsonNode> T getChild(String key) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JsonNode> getChildren(String... names) {
		return Collections.EMPTY_LIST;
	}
}
