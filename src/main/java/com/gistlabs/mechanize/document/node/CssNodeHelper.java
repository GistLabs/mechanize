/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.node;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import com.gistlabs.mechanize.util.css_query.NodeHelper;


public class CssNodeHelper implements NodeHelper<Node> {
	
	private Node root;

	public CssNodeHelper(Node root) {
		this.root = root;
	}

    public String getValue(Node element) {
		return element.getValue();
	}
	
	public boolean hasAttribute(Node element, String name) {
		return element.hasAttribute(name);
	}
	
	public String getAttribute(Node element, String name) {
		if (element.hasAttribute(name))
			return element.getAttribute(name);
		else 
			return null;
	}
    
    public Collection<? extends Node> getDescendentNodes(Node node) {
    	Collection<Node> result = new LinkedHashSet<Node>();
    	result.add(node);
    	
    	LinkedList<Node> toProcess = new LinkedList<Node>();
    	toProcess.add(node);
    	while(!toProcess.isEmpty()) {
    		Node first = toProcess.removeFirst();
    		List<? extends Node> children = first.getChildren();
    		result.addAll(children);
    		toProcess.addAll(children);
    	}
    	
    	return result;
	}
    
    public List<? extends Node> getChildNodes(Node node) {
		return node.getChildren();
	}
    
    @Override
    public boolean isEmpty(Node node) {
    	return getChildNodes(node).isEmpty();
    }

    public String getName(Node n) {
		return n.getName();
	}
    
    public Node getNextSibling(Node node) {
    	throw new UnsupportedOperationException("Haven't implemented this yet");
    	//DOMHelper.getNextSiblingElement(node);
		// TODO Auto-generated method stub
	}

    @SuppressWarnings("unchecked")
	public Index getIndexInParent(Node node, boolean byType) {
		String type = byType ? node.getName() : "*";
		
		List<? extends Node> children;
		Node parent = node.getParent();
		if (parent==null)
			children = Collections.EMPTY_LIST;
		else
			children = parent.getChildren(type);

		return new Index(children.indexOf(node), children.size());
	}

	public Node getRoot() {
		return this.root;
	}
	
	@Override
	public boolean nameMatches(Node n, String name) {
		return "*".equals(name) || n.getName().equalsIgnoreCase(name);
	}	
}
