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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.damnhandy.uri.template.UriTemplate;

/**
 * Wraps a JSON node with Link generatation behavior. See <a href="https://github.com/GistLabs/mechanize/wiki/JSON-Linking">https://github.com/GistLabs/mechanize/wiki/JSON-Linking</a>
 *
 */
public class JsonLink {

	private final JsonNode node;
	private final String attrName;
	private final URL baseUrl;
	private Map<String, Object> variables = new HashMap<String, Object>();

	public JsonLink(JsonNode node) {
		this(null, node, "href");
	}

	public JsonLink(String baseUrl, JsonNode node, String attrName) {
		if (node==null || attrName==null) throw new NullPointerException(String.format("baseUrl=%s, node=%s, attributeName=%s", baseUrl, node, attrName));
		if (!node.hasAttribute(attrName)) throw new IllegalArgumentException(String.format("Node %s does not have an attribute named %s", node, attrName));
		
		this.node = node;
		this.baseUrl = baseUrl(baseUrl);
		this.attrName = attrName;
	}

	public String name() {
		if (node.hasAttribute("rel")) return node.getAttribute("rel");
		
		//else 
		return node.getName();
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
		return template(buildTemplate());
	}

	protected UriTemplate buildTemplate() {
		try {
			return UriTemplate.fromTemplate(combine(raw()));
		} catch (Exception e) {
			throw new RuntimeException(String.format("Problem building UriTemplate"), e);
		}
	}
	
	protected String template(UriTemplate template) {
		try {			
			String[] variables = template.getVariables();
			if (variables.length>0) {
				for (String var : variables) {
					template.set(var, lookupVar(var));
				}				
			}
			
			return template.expand();
		} catch (Exception e) {
			throw new RuntimeException(String.format("Problem processing %s", template), e);
		}
	}

	public String[] getVariables() {
		return buildTemplate().getVariables();
	}

	protected Object lookupVar(String var) {
		if (variables.containsKey(var)) {
			return variables.get(var);
		} else {
			return lookupWalkForVar(var);
		}
	}
	
	/**
	 * Return an Object that is either String or List<String> or Map<String, String>.
	 * 
	 * @param var maybe null if not found
	 * @return
	 */
	protected Object lookupWalkForVar(String var) {
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
	 * Set value for a template link. Can be Object, List<String> and Map<String,String>, objects need to have .toString() method.
	 * @param name
	 * @param value
	 */
	public void set(String name, Object value) {
		this.variables.put(name, value);
	}

	/**
	 * Set multiple values for a template link. Values in Map can be List<Object> and Map<Object>, objects need to have .toString() method.
	 * @param values
	 */
	public void setAll(Map<String, Object> values) {
		this.variables.putAll(values);
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
		return node.getAttribute(attrName);
	}

	public JsonNode node() {
		return node;
	}

}
