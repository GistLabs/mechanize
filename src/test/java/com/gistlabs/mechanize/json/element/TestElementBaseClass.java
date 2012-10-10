package com.gistlabs.mechanize.json.element;

import org.json.JSONException;
import org.json.JSONObject;

import com.gistlabs.mechanize.json.exceptions.JsonException;

public abstract class TestElementBaseClass {

	public TestElementBaseClass() {
		super();
	}

	protected JSONObject parseJson(String json) {
		try {
			return new JSONObject(json);
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

}