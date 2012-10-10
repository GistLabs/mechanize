package com.gistlabs.mechanize.json.query.matchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import se.fishtank.css.selectors.Selector;

import com.gistlabs.mechanize.json.Node;
import com.gistlabs.mechanize.json.query.Matcher;

public class TagMatcher extends AbstractTagMatcher<Node> implements Matcher<Node> {
   
	public TagMatcher(Selector selector) {
        super(selector);
    }
     
    protected Collection<? extends Node> getDescendentNodes(Node node) {
    	List<Node> result = new ArrayList<Node>();
    	
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
    
    protected Collection<? extends Node> getChildNodes(Node node) {
		return node.getChildren();
	}

	protected String getName(Node n) {
		return n.getName();
	}
    
    protected Node getNextSibling(Node node) {
    	throw new UnsupportedOperationException("Haven't implemented this yet");
    	//DOMHelper.getNextSiblingElement(node);
		// TODO Auto-generated method stub
	}
}
