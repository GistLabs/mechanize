package com.gistlabs.mechanize.json.exceptions;

import org.json.JSONArray;

public class JsonArrayException extends JsonException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 253115664519387324L;
	private JSONArray array;

	public JsonArrayException() {
	}

	public JsonArrayException(String arg, JSONArray array) {
		super(arg);
		this.array = array;
	}

	public JsonArrayException(Throwable th) {
		super(th);
	}

	public JsonArrayException(String arg, Throwable th) {
		super(arg, th);
	}

	public JSONArray getArray() {
		return array;
	}
}
