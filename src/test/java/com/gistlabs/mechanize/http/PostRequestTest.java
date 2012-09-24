package com.gistlabs.mechanize.http;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import com.gistlabs.mechanize.MechanizeTestCase;
import com.gistlabs.mechanize.Page;

public class PostRequestTest extends MechanizeTestCase {
	
	@SuppressWarnings("serial")
	@Test
	public void testPostRequest() throws Exception {
		agent.addPageRequest("POST", "http://test.com/form", parameter("test", "Test"), newHtml("OK", ""));
		
		Page result = agent.post("http://test.com/form", new HashMap<String,String>(){{put("test","Test");}});
		assertEquals("OK", result.getTitle());
	}
}
