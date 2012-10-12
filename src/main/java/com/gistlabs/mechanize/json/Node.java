package com.gistlabs.mechanize.json;

import java.util.Collection;
import java.util.List;

/**
 * JSON Element abstraction, like the DOM for XML
 * 
 */
public interface Node {

	public String getName();
	
	public String getAttribute(String key);
	public void setAttribute(String key, String value);
	public boolean hasAttribute(String key);
	public Collection<String> getAttributes();
	
	public String getValue();
	public void setValue(String value);
	
	public <T extends Node> T getChild(String key);
	public List<? extends Node> getChildren();
	public List<? extends Node> getChildren(String key);

	
	public <T extends Node> T find(String query);
	public List<? extends Node> findAll(String query);

	public <T extends Node> T getParent();
}
