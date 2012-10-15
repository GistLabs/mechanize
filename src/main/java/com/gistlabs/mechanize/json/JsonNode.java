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
	
	/**
	 * Find exactly one child with name, otherwise throw exception
	 * 
	 * @param name
	 * @return Either null, the one child node with name, or exception because of multiple
	 */
	public <T extends JsonNode> T getChild(String name);
	
	/**
	 * Get children of this node. The names argument allow to filter the list of returned children 
	 * to only those that match getName() in names.
	 * 
	 * The special name, '*', is the equivalent of matching all child names.
	 * 
	 * @param names
	 * @return
	 */
	public List<? extends JsonNode> getChildren(String... names);
	
	public <T extends JsonNode> T find(String query);
	public List<? extends JsonNode> findAll(String query);

	public <T extends JsonNode> T getParent();
}
