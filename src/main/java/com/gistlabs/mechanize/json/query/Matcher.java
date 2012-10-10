package com.gistlabs.mechanize.json.query;

import java.util.Collection;

public interface Matcher<Node> {
    public abstract Collection<Node> match(Collection<Node> nodes, Node root);

}
