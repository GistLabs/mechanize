package com.gistlabs.mechanize.json.nodeImpl;

import java.util.*;

import com.gistlabs.mechanize.json.Node;
import com.gistlabs.mechanize.json.query.NodeHelper;


public class JsonNodeHelper implements NodeHelper<Node> {

    public String getValue(Node element) {
		return element.getContent();
	}
	
    public boolean hasAttribute(Node element, String name) {
		return element.hasAttribute(name);
	}

    public Collection<Node> getAttributes(Node element) {
		Collection<Node> result = new LinkedHashSet<Node>();
		for(String key : element.getAttributes()) {
			result.add(element.getChild(key));
		}
		return result;
	}
    
    public Collection<? extends Node> getDescendentNodes(Node node) {
    	Collection<Node> result = new LinkedHashSet<Node>();
    	
    	LinkedList<Node> toProcess = new LinkedList<Node>();
    	toProcess.add(node);
    	while(!toProcess.isEmpty()) {
    		Node first = toProcess.removeFirst();
    		List<Node> children = first.getChildren();
    		result.addAll(children);
    		toProcess.addAll(children);
    	}
    	
    	return result;
	}
    
    public Collection<? extends Node> getChildNodes(Node node) {
		return node.getChildren();
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
		
		List<Node> children;
		Node parent = node.getParent();
		if (parent==null)
			children = Collections.EMPTY_LIST;
		else
			children = parent.getChildren(type);

		return new Index(children.indexOf(node), children.size());
	}

	public Node getRoot(Node node) {
		Node root = node;
		while (root.getParent()!=null)
			root = root.getParent();
		return root;
	}

}
