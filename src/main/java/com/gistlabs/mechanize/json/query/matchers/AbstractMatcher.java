package com.gistlabs.mechanize.json.query.matchers;

import java.util.Collection;

import se.fishtank.css.util.Assert;

import com.gistlabs.mechanize.json.query.Matcher;
import com.gistlabs.mechanize.json.query.NodeHelper;

public abstract class AbstractMatcher<Node> implements Matcher<Node> {

	protected final NodeHelper<Node> helper;

    /** The set of nodes to check. */
    protected Collection<Node> nodes;
    
    /** The result of the checks. */
    protected Collection<Node> result;
    
	public AbstractMatcher(NodeHelper<Node> helper) {
        Assert.notNull(helper, "helper is null!");
		this.helper = helper;
	}
}
