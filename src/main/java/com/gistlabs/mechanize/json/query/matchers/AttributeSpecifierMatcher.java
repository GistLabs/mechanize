package com.gistlabs.mechanize.json.query.matchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.fishtank.css.selectors.specifier.AttributeSpecifier;
import se.fishtank.css.util.Assert;

import com.gistlabs.mechanize.json.Element;
import com.gistlabs.mechanize.json.query.Matcher;

public class AttributeSpecifierMatcher implements Matcher<Element> {
	
    /** The attribute specifier to check against. */
    private final AttributeSpecifier specifier;
   
    /**
     * Create a new instance.
     * 
     * @param specifier The attribute specifier to check against. 
     */
    public AttributeSpecifierMatcher(AttributeSpecifier specifier) {
        Assert.notNull(specifier, "specifier is null!");
        this.specifier = specifier;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Element> match(Collection<Element> nodes, Element root) {
        Assert.notNull(nodes, "nodes is null!");
        Collection<Element> result = new ArrayList<Element>();
        for (Element node : nodes) {
        	// It just have to be present.
            String name = specifier.getName();
			if (specifier.getValue() == null && hasAttribute(node, name)) {
                result.add(node);
                continue;
            }
            
            Collection<Element> attributes = getAttributes(node);
            for (Element element : attributes) {
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

	protected String getValue(Element element) {
		return element.getContent();
	}
	
	protected boolean hasAttribute(Element element, String name) {
		return element.hasAttribute(name);
	}

	protected Collection<Element> getAttributes(Element element) {
		List<Element> result = new ArrayList<Element>();
		for(String key : element.getAttributes()) {
			result.add(element.getChild(key));
		}
		return result;
	}
}
