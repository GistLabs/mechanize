package com.gistlabs.mechanize.json;

import java.util.Collection;
import java.util.List;

/**
 * JSON Element abstraction, like the DOM for XML
 */
public interface Element {

	public String getName();
	
	public String getAttribute(String key);
	public void setAttribute(String key, String value);
	public boolean hasAttribute(String key);
	public Collection<String> getAttributes();
	
	public String getContent();
	public void setContent(String value);
	
	public Element getChild(String key);
	public List<Element> getChildren();

	
	public Element find(String query);
	public List<Element> findAll(String query);

	public Element getParent();
}
