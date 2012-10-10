package com.gistlabs.mechanize.json.query.matchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.fishtank.css.selectors.specifier.AttributeSpecifier;

import com.gistlabs.mechanize.json.Element;
import com.gistlabs.mechanize.json.query.Matcher;

public class AttributeSpecifierMatcher extends AbstractAttributeSpecifierMatcher<Element> implements Matcher<Element> {
	
    /**
     * Create a new instance.
     * 
     * @param specifier The attribute specifier to check against. 
     */
    public AttributeSpecifierMatcher(AttributeSpecifier specifier) {
    	super(specifier);
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
