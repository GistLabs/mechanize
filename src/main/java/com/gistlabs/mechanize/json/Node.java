package com.gistlabs.mechanize.json;

import java.util.Collection;
import java.util.List;

/**
 * JSON Element abstraction, like the DOM for XML
 */
public interface Node {

	public String getName();
	
	public String getAttribute(String key);
	public void setAttribute(String key, String value);
	public boolean hasAttribute(String key);
	public Collection<String> getAttributes();
	
	public String getContent();
	public void setContent(String value);
	
	public Node getChild(String key);
	public List<Node> getChildren();
	public List<Node> getChildren(String key);

	
	public Node find(String query);
	public List<Node> findAll(String query);

	public Node getParent();
}
