package com.gistlabs.mechanize;

import org.apache.http.client.methods.HttpRequestBase;

public interface RequestBuilderFactory {
	public RequestBuilder requestBuilder(String uri);
}
