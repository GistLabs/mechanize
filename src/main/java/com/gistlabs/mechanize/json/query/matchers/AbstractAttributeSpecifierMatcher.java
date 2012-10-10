package com.gistlabs.mechanize.json.query.matchers;

import java.util.ArrayList;
import java.util.Collection;

import se.fishtank.css.selectors.specifier.AttributeSpecifier;
import se.fishtank.css.util.Assert;

import com.gistlabs.mechanize.json.query.Matcher;

public abstract class AbstractAttributeSpecifierMatcher<Node> implements Matcher<Node> {

	/** The attribute specifier to check against. */
	protected final AttributeSpecifier specifier;

	public AbstractAttributeSpecifierMatcher(AttributeSpecifier specifier) {
        Assert.notNull(specifier, "specifier is null!");
        this.specifier = specifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Node> match(Collection<Node> nodes) {
	    Assert.notNull(nodes, "nodes is null!");
	    Collection<Node> result = new ArrayList<Node>();
	    for (Node node : nodes) {
	    	// It just have to be present.
	        String name = specifier.getName();
			if (specifier.getValue() == null && hasAttribute(node, name)) {
	            result.add(node);
	            continue;
	        }
	        
	        Collection<Node> attributes = getAttributes(node);
	        for (Node element : attributes) {
	        	String value = getValue(element);
	            String spec = specifier.getValue();
	            switch (specifier.getMatch()) {
	            case EXACT:
	                if (value.equals(spec)) {
	                    result.add(node);
	                }
	                
	                break;
	            case HYPHEN:
	                if (value.equals(spec) || value.startsWith(spec + '-')) {
	                    result.add(node);
	                }
	                
	                break;
	            case PREFIX:
	                if (value.startsWith(spec)) {
	                    result.add(node);
	                }
	                
	                break;
	            case SUFFIX:
	                if (value.endsWith(spec)) {
	                    result.add(node);
	                }
	                
	                break;
	            case CONTAINS:
	                if (value.contains(spec)) {
	                    result.add(node);
	                }
	                
	                break;
	            case LIST:
	                for (String v : value.split("\\s+")) {
	                    if (v.equals(spec)) {
	                        result.add(node);
	                    }
	                }
	                
	                break;
	            }
	        }
	    }
	    
	    return result;
	}
    
    protected abstract String getValue(Node element);
	
	protected abstract boolean hasAttribute(Node element, String name);

	protected abstract Collection<Node> getAttributes(Node element);

}