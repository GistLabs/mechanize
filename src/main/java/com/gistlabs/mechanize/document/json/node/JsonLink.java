package com.gistlabs.mechanize.document.json.node;

import java.net.MalformedURLException;
import java.net.URL;


public class JsonLink {

	private JsonNode node;
	private URL baseUrl;

	public JsonLink(JsonNode node) {
		this.node = node;
	}

	public JsonLink(String baseUrl, JsonNode node) {
		this(node);
		setBaseUrl(baseUrl);
	}

	protected void setBaseUrl(String baseUrl) {
		try {
			this.baseUrl = new URL(baseUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(String.format("Problem with %s", baseUrl), e);
		}
	}

	public String uri() {
		String raw = raw();
		
		return baseUrl!=null ? combine(raw).toExternalForm() : raw;
	}

	protected URL combine(String raw) {
		try {
			return new URL(baseUrl, raw);
		} catch (MalformedURLException e) {
			throw new RuntimeException(String.format("Problem conbining %s with %s", baseUrl, raw), e);
		}
	}

	public String raw() {
		return node.getAttribute("href");
	}

	public JsonNode node() {
		return node;
	}

}
