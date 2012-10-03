package com.gistlabs.mechanize;

import org.apache.http.client.methods.HttpRequestBase;

public interface PageRequestor {
	public Page request(HttpRequestBase request);
}
