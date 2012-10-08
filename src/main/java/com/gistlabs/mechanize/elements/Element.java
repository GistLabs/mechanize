package com.gistlabs.mechanize.elements;

import java.util.Collection;
import java.util.List;

import com.gistlabs.mechanize.requestor.RequestBuilderFactory;

/**
 * An abstraction over HTML, XML, and JSON element/node types.
 * 
 * This is intended to be easy to adapt and provide generic data inspection.
 * 
 * @author jheintz
 *
 */
public interface Element<Page extends RequestBuilderFactory<Page>> extends Iterable<Element<Page>> {

	public Page getPage();
	
	public String getElementName();
	
	public String getAttribute(String key);
	public void setAttribute(String key, String value);
	public boolean hasAttribute(String key);
	public Collection<String> getAttributes();
	
	public String getContent();
	public void setContent(String value);
	
	public List<Element<Page>> getChildren();
	
	public Element<Page> get(String query);
	public List<Element<Page>> getAll(String query);
}
