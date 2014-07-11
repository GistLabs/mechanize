/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.node;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.damnhandy.uri.template.UriTemplate;


public class JsonLink {

	private final JsonNode node;
	private final URL baseUrl;

	public JsonLink(JsonNode node) {
		this(null, node);
	}

	public JsonLink(String baseUrl, JsonNode node) {
		if (node==null) throw new NullPointerException(String.format("baseUrl=%s, node=%s", baseUrl, node));
		
		this.node = node;
		this.baseUrl = baseUrl(baseUrl);
	}

	protected URL baseUrl(String baseUrl) {
		if (baseUrl==null) return null;
		
		try {
			return new URL(baseUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(String.format("Problem with %s", baseUrl), e);
		}
	}

	public String uri() {
		String raw = raw();
		String combined = combine(raw);
		
		String expanded = template(combined);
		
		return expanded;
	}

	protected String template(String combined) {
		try {
			UriTemplate template = UriTemplate.fromTemplate(combined);
			
			String[] variables = template.getVariables();
			if (variables.length>0) {
				for (String var : variables) {
					template.set(var, lookupVar(var));
				}				
			}
			
			return template.expand();
		} catch (Exception e) {
			throw new RuntimeException(String.format("Problem processing %s", combined), e);
		}
	}

	/**
	 * Return an Object that is either String or List<String> or Map<String, String>.
	 * 
	 * 
	 * 
	 * @param var maybe null if not found
	 * @return
	 */
	protected Object lookupVar(String var) {
		JsonNode current = node;
		while (current!=null) {
			Object result = lookupVar(current, var);
			if (result!=null) return result;
			
			if (current.hasAttribute("inheritProperties")) {
				current = current.getParent();				
			} else {
				current = null;
			}
		}
		return lookupVar(node, var);
	}
	
	/**
	 * Return an Object that is either String or List<String> or Map<String, String>
	 * @param var maybe null if not found
	 * @return
	 */
	protected Object lookupVar(JsonNode node, String var) {
		List<? extends JsonNode> children = node.getChildren(var);
		if (children.size()>1) { // multiple with name, treat as list of values
			List<String> result = new ArrayList<String>();
			for (JsonNode child : children) {
				result.add(child.getValue());
			}
			return result;
		} else if (children.size()==1) {
			JsonNode child = children.get(0);
			List<String> attributeNames = child.getAttributeNames();
			Collections.sort(attributeNames);

			if (attributeNames.size()==0) { // treat child as the attribute value
				return child.getValue();
			} else { // treat child as object with map values
				Map<String, String> result = new LinkedHashMap<String, String>();
				for (String attrName : attributeNames) {
					result.put(attrName, child.getAttribute(attrName));
				}
				return result;
			}
		} else { // return null
			return null;
		}
	}

	protected String combine(String raw) {
		if (baseUrl==null) {
			return raw;
		} else {
			try {
				return new URL(baseUrl, raw).toExternalForm();
			} catch (MalformedURLException e) {
				throw new RuntimeException(String.format("Problem conbining %s with %s", baseUrl, raw), e);
			}
		}
	}

	public String raw() {
		return node.getAttribute("href");
	}

	public JsonNode node() {
		return node;
	}

}
