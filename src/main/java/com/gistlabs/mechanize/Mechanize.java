package com.gistlabs.mechanize;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.impl.client.AbstractHttpClient;

import com.gistlabs.mechanize.cookie.Cookies;
import com.gistlabs.mechanize.parameters.Parameters;
import com.gistlabs.mechanize.requestor.RequestBuilder;

public interface Mechanize {

	static final String MECHANIZE_LOCATION = "MechanizeLocation";

	public abstract AbstractHttpClient getClient();

	public abstract RequestBuilder<Resource> doRequest(String uri);

	public abstract <T extends Resource> T get(String uri);

	public abstract <T extends Resource> T post(String uri,
			Map<String, String> params) throws UnsupportedEncodingException;

	/**
	 * POST either URL encoded or multi-part encoded content body, based on presence of file content body parameters
	 * @param uri
	 * @param params
	 * @return
	 */
	public abstract <T extends Resource> T post(String uri, Parameters params);

	public abstract Cookies cookies();

}