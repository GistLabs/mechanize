package com.gistlabs.mechanize.json.query;

import java.util.Collection;

public interface Checker<Node> {
    public abstract Collection<Node> check(Collection<Node> nodes);

}
