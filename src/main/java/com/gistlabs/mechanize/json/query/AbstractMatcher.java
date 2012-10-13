package com.gistlabs.mechanize.json.query;

import java.util.Collection;

import se.fishtank.css.util.Assert;


public abstract class AbstractMatcher<JsonNode> implements Matcher<JsonNode> {

	protected final NodeHelper<JsonNode> helper;

    /** The set of nodes to check. */
    protected Collection<JsonNode> nodes;
    
    /** The result of the checks. */
    protected Collection<JsonNode> result;
    
	public AbstractMatcher(NodeHelper<JsonNode> helper) {
        Assert.notNull(helper, "helper is null!");
		this.helper = helper;
	}
}
