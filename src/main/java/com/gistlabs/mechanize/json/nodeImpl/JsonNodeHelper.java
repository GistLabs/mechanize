package com.gistlabs.mechanize.json.nodeImpl;

import java.util.*;

import com.gistlabs.mechanize.json.JsonNode;
import com.gistlabs.mechanize.json.query.NodeHelper;


public class JsonNodeHelper implements NodeHelper<JsonNode> {

    public String getValue(JsonNode element) {
		return element.getValue();
	}
	
	public JsonNode getAttribute(JsonNode element, String name) {
		if (element.hasAttribute(name))
			return element.getChild(name);
		else 
			return null;
	}

    public Collection<JsonNode> getAttributes(JsonNode element) {
		Collection<JsonNode> result = new LinkedHashSet<JsonNode>();
		for(String key : element.getAttributes()) {
			result.add(element.getChild(key));
		}
		return result;
	}
    
    public Collection<? extends JsonNode> getDescendentNodes(JsonNode node) {
    	Collection<JsonNode> result = new LinkedHashSet<JsonNode>();
    	result.add(node);
    	
    	LinkedList<JsonNode> toProcess = new LinkedList<JsonNode>();
    	toProcess.add(node);
    	while(!toProcess.isEmpty()) {
    		JsonNode first = toProcess.removeFirst();
    		List<? extends JsonNode> children = first.getChildren();
    		result.addAll(children);
    		toProcess.addAll(children);
    	}
    	
    	return result;
	}
    
    public Collection<? extends JsonNode> getChildNodes(JsonNode node) {
		return node.getChildren();
	}
    
    @Override
    public boolean isEmpty(JsonNode node) {
    	return getChildNodes(node).isEmpty();
    }

    public String getName(JsonNode n) {
		return n.getName();
	}
    
    public JsonNode getNextSibling(JsonNode node) {
    	throw new UnsupportedOperationException("Haven't implemented this yet");
    	//DOMHelper.getNextSiblingElement(node);
		// TODO Auto-generated method stub
	}

    @SuppressWarnings("unchecked")
	public Index getIndexInParent(JsonNode node, boolean byType) {
		String type = byType ? node.getName() : "*";
		
		List<? extends JsonNode> children;
		JsonNode parent = node.getParent();
		if (parent==null)
			children = Collections.EMPTY_LIST;
		else
			children = parent.getChildren(type);

		return new Index(children.indexOf(node), children.size());
	}

	public JsonNode getRoot(JsonNode node) {
		JsonNode root = node;
		while (root.getParent()!=null)
			root = root.getParent();
		return root;
	}

	@Override
	public boolean namesEqual(String tag1, String tag2) {
		return tag1.equalsIgnoreCase(tag2);
	}
}
