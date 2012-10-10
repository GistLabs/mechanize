package com.gistlabs.mechanize.json.query.matchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.fishtank.css.selectors.specifier.AttributeSpecifier;

import com.gistlabs.mechanize.json.Node;
import com.gistlabs.mechanize.json.query.Matcher;

public class AttributeSpecifierMatcher extends AbstractAttributeSpecifierMatcher<Node> implements Matcher<Node> {
	
    /**
     * Create a new instance.
     * 
     * @param specifier The attribute specifier to check against. 
     */
    public AttributeSpecifierMatcher(AttributeSpecifier specifier) {
    	super(specifier);
    }
    
    protected String getValue(Node element) {
		return element.getContent();
	}
	
	protected boolean hasAttribute(Node element, String name) {
		return element.hasAttribute(name);
	}

	protected Collection<Node> getAttributes(Node element) {
		List<Node> result = new ArrayList<Node>();
		for(String key : element.getAttributes()) {
			result.add(element.getChild(key));
		}
		return result;
	}
}
