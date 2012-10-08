package com.gistlabs.mechanize.elements.spi;

import java.util.Collection;
import java.util.List;

/**
 * The SPI (Service Provider Interface) is the backend interface needed to connect with various implementations.
 * 
 * @author jheintz
 *
 */
public interface Element {
	public String getElementName();
	public String getAttribute(String key);
	public void setAttribute(String key, String value);
	public boolean hasAttribute(String key);
	public Collection<String> getAttributes();
	
	public String getContent();
	public void setContent(String value);
	
	public List<? extends Element> getChildren();
}
