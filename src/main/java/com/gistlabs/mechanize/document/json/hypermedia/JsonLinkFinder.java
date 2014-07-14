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
import com.gistlabs.mechanize.document.node.Node;
import com.gistlabs.mechanize.document.node.NodeVisitor;

/**
 * Search JSON nodes to find hyperlink defintions in the data. This is based on pattern matching attribute names.
 * 
 * This class will find attributes that match either exact string (i.e. "href"), or patterns (i.e. "more-href").
 * 
 * Also, in the case of exact matches then the link relation will be looked up by heuristic. Either the "rel" 
 * attribute is used, if found, or the name of the containing json object if present. For pattern matched attributes
 * the prefix before the pattern is used as the rel.
 * 
 */
public class JsonLinkFinder {

	static class Match {
		final String attrName;
		final String rel;

		public Match(String attrName, String rel) {
			this.attrName = attrName;
			this.rel = rel;
		}
	}
	
	private final Set<String> linkingNames = new HashSet<String>(Arrays.asList("href", "uri"));

	public List<JsonLink> findRecursive(JsonNode node) {
		if (node==null) throw new NullPointerException(String.format("root=%s", node));

		final List<JsonLink> result = new ArrayList<JsonLink>();
		
		node.visit(new NodeVisitor() {

			@Override
			public boolean beginNode(Node node) {
				findOn((JsonNode)node, result);
				return true;
			}

			@Override
			public void endNode(Node node) {
				// no op
			}
			
		});
		
		return result;
	}
	
	public List<JsonLink> findWithChildren(JsonNode node) {
		if (node==null) throw new NullPointerException(String.format("root=%s", node));

		List<JsonLink> result = new ArrayList<JsonLink>();
		
		findOn(node, result);
		for (JsonNode child : node.getChildren()) {
			findOn(child, result);
		}
		
		return result;
	}
	
	public List<JsonLink> findOn(JsonNode node) {
		if (node==null) throw new NullPointerException(String.format("root=%s", node));
		
		List<JsonLink> result = new ArrayList<JsonLink>();
		
		findOn(node, result);

		return result;
	}
	
	void findOn(JsonNode node, List<JsonLink> result) {
		List<String> attributeNames = node.getAttributeNames();
		for (String attrName : attributeNames) {
			Match match = matches(attrName);
			if (match!=null) {
				result.add(build(node, match));
			}
		}
	}

	Match matches(String attrName) {
		Match exactMatch = exactMatch(attrName);
		if (exactMatch!=null) return exactMatch;
		
		return patternMatch(attrName);
	}

	Match exactMatch(String attrName) {
		if (linkingNames.contains(attrName.toLowerCase())) {
			return new Match(attrName, null);
		} else {
			return null;
		}
	}

	Match patternMatch(String attrName) {
		for (String linkingName : linkingNames) {
			if (attrName.endsWith("-"+linkingName) || attrName.endsWith("_"+linkingName)) {
				String prefix = attrName.substring(0, attrName.length()-linkingName.length()-1); // subtract the last one for the - or _
				return new Match(attrName, prefix);
			}
		}
		return null;
	}

	JsonLink build(JsonNode node, Match match) {
		return new JsonLink(null, node, match.attrName, match.rel);
	}
}
