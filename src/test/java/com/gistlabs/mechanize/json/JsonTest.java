package com.gistlabs.mechanize.json;

import static org.junit.Assert.*;

import java.io.InputStreamReader;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;

public class JsonTest {

	/**
	 * Example from https://www.dropbox.com/developers/reference/api#account-info in dropbox.account.info.json
	 */
	@Test
	public void testJsonParsing() throws Exception {
		JSONObject json = new JSONObject(new JSONTokener(new InputStreamReader(getClass().getResourceAsStream("dropbox.account.info.json"))));
		assertNotNull(json);
		assertEquals(12345678, json.getLong("uid"));
	}

}
