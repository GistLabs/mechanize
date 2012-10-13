package com.gistlabs.mechanize.json.query;

import java.util.Collection;

public interface Matcher<JsonNode> {
    public abstract Collection<JsonNode> match(Collection<JsonNode> nodes);

}
