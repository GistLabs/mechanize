package com.gistlabs.mechanize.json;

import java.util.Collection;
import java.util.List;

/**
 * JSON Element abstraction, like the DOM for XML
 * 
 */
public interface JsonNode {

	public String getName();
	
	public String getAttribute(String key);
	public void setAttribute(String key, String value);
	public boolean hasAttribute(String key);
	public Collection<String> getAttributes();
	 
	public String getValue();
	public void setValue(String value);
	
	public <T extends JsonNode> T getChild(String key);
	public List<? extends JsonNode> getChildren();
	public List<? extends JsonNode> getChildren(String key);

	
	public <T extends JsonNode> T find(String query);
	public List<? extends JsonNode> findAll(String query);

	public <T extends JsonNode> T getParent();
}
