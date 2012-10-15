package com.gistlabs.mechanize.util.css_query;

import java.util.Collection;

public interface Checker<Node> {
    public abstract Collection<Node> check(Collection<Node> nodes);

}
