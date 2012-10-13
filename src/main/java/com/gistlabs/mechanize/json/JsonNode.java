package com.gistlabs.mechanize.json;

import java.util.Collection;
import java.util.List;

/**
 * JSON Element abstraction, like the DOM for XML
 */
public interface JsonNode {

	public String getName();
	
	public String getAttribute(String key);
	public void setAttribute(String key, String value);
	public boolean hasAttribute(String key);
	public Collection<String> getAttributes();
	
	public String getContent();
	public void setContent(String value);
	
	public JsonNode getChild(String key);
	public List<JsonNode> getChildren();
	public List<JsonNode> getChildren(String key);

	
	public JsonNode find(String query);
	public List<JsonNode> findAll(String query);

	public JsonNode getParent();
}
