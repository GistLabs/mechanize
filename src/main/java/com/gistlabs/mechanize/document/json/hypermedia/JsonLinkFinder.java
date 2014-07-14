/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.hypermedia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gistlabs.mechanize.document.json.node.JsonNode;

/**
 * Search JSON nodes to find hyperlink defintions in the data. This is based on pattern matching attribute names.
 */
public class JsonLinkFinder {
	private final Set<String> linkingNames = new HashSet<String>(Arrays.asList("href", "uri"));

	public List<JsonLink> findOn(JsonNode node) {
		if (node==null) throw new NullPointerException(String.format("root=%s", node));
		
		List<JsonLink> result = new ArrayList<JsonLink>();
		
		List<String> attributeNames = node.getAttributeNames();
		for (String attrName : attributeNames) {
			if (matches(attrName)) {
				result.add(build(node, attrName));
			}
		}
		
		return result;
	}

	boolean matches(String attrName) {
		return exactMatch(attrName) || patternMatch(attrName);
	}

	boolean exactMatch(String attrName) {
		return linkingNames.contains(attrName.toLowerCase());
	}

	boolean patternMatch(String attrName) {
		for (String linkingName : linkingNames) {
			if (attrName.endsWith("-"+linkingName) || attrName.endsWith("_"+linkingName)) {
				return true;
			}
		}
		return false;
	}

	JsonLink build(JsonNode node, String attrName) {
		return new JsonLink(node, attrName);
	}
}
