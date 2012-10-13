package com.gistlabs.mechanize.json.query;

import java.util.Collection;

public interface NodeHelper<JsonNode> {
    
    public class Index {
    	public final int index;
    	public final int size;
    	public Index(int index, int size) {
    		this.index = index;
    		this.size = size;
    	}
	}

    public String getValue(JsonNode element);
	
	public boolean hasAttribute(JsonNode element, String name);

	public Collection<JsonNode> getAttributes(JsonNode element);

	public Index getIndexInParent(JsonNode node, boolean byType);

	public JsonNode getRoot(JsonNode node);


    public Collection<? extends JsonNode> getDescendentNodes(JsonNode node);
    
    public Collection<? extends JsonNode> getChildNodes(JsonNode node);

	public String getName(JsonNode n);
    
    public JsonNode getNextSibling(JsonNode node);
    
}
