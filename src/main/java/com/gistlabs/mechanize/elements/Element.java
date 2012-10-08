package com.gistlabs.mechanize.elements;

import java.util.Collection;
import java.util.List;

/**
 * An abstraction over HTML, XML, and JSON element/node types.
 * 
 * This is intended to be easy to adapt and provide generic data inspection.
 * 
 * @author jheintz
 *
 */
public interface Element<Source> {

	public Source getSource();
	
	public String getAttribute(String key);
	public void setAttribute(String key, String value);
	public boolean hasAttribute(String key);
	public Collection<String> getAttributes();
	
	public String getValue();
	public void setValue();
	
	public List<Element<Source>> getChildren();
	
}
