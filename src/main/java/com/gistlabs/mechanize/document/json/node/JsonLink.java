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
					template.set(var, node.getAttribute(var));
				}				
			}
			
			return template.expand();
		} catch (Exception e) {
			throw new RuntimeException(String.format("Problem processing %s", combined), e);
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
