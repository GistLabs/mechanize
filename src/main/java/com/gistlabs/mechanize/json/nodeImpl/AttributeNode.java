package com.gistlabs.mechanize.json.nodeImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.gistlabs.mechanize.json.Node;

public class AttributeNode extends AbstractNode {
	
	public AttributeNode(Node parent, String name) {
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
	public Collection<String> getAttributes() {
		return null;
	}

	@Override
	public String getContent() {
		return this.parent.getAttribute(getName());
	}

	@Override
	public void setContent(String value) {
		this.parent.setAttribute(getName(), value);
	}

	@Override
	public Node getChild(String key) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Node> getChildren() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<Node> getChildren(String key) {
		return getChildren();
	}
}
