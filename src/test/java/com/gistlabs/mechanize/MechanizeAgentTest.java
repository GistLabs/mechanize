/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Test;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.Page;
import com.gistlabs.mechanize.RequestInterceptor;
import com.gistlabs.mechanize.ResponseInterceptor;
import com.gistlabs.mechanize.parameters.Parameters;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class MechanizeAgentTest extends MechanizeTestCase {
	@Test
	public void testReceivingAPage() {
		agent.addPageRequest("http://test.com", newHtml("Test Page", ""));
		Page page = agent.get("http://test.com");
		assertEquals("Test Page", page.getTitle());
	}
	
    @Test(expected=IllegalArgumentException.class)
    public void testExpectPostButReceiveGetRequestFails() throws Exception {
        agent.addPageRequest("POST", "http://test.com/form", newHtml("OK", ""));
        disableAfterTest();
        
        Page result = agent.get("http://test.com/form");
        assertEquals("OK", result.getTitle());
    }   
    
    @Test
    public void testRequestParametersExtractedFromUri() {
    	Parameters parameters = agent.doRequest("http://www.test.com/index.html?query=ab+cd&page=1").parameters();
    	assertEquals("ab cd", parameters.get("query")[0]);
    	assertEquals("1", parameters.get("page")[0]);
    }
    
	@Test
	public void testDoRequestGet() {
		agent.addPageRequest("GET", "http://test.com/index.html?query=ab+cd&page=1", newHtml("Test Page", ""));
		Page page = agent.doRequest("http://test.com/index.html").add("query", "ab cd").add("page", "1").get();
		assertEquals("Test Page", page.getTitle());
	}

	@Test
	public void testDoRequestGetWithExistingQueryParameters() {
		agent.addPageRequest("GET", "http://test.com/index.html?query=ab+cd&page=2", newHtml("Test Page", ""));
		Page page = agent.doRequest("http://test.com/index.html?query=ab+cd&page=1").set("page", "2").get();
		assertEquals("Test Page", page.getTitle());
	}

	@Test
	public void testDoRequestPostWithExistingQueryParameters() {
		Parameters expectedParameters = new Parameters().add("query","ab cd").add("page", "2");
		agent.addPageRequest("Post", "http://test.com/index.html", expectedParameters, newHtml("Test Page", ""));
		Page page = agent.doRequest("http://test.com/index.html?query=ab+cd&page=1").set("page", "2").post();
		assertEquals("Test Page", page.getTitle());
	}
    
	@Test
	public void testPostMethod() {
		MechanizeAgent agent = new MechanizeAgent();
		Parameters parameters = new Parameters().add("param1", "value").add("param2", "value2");
		Page page = agent.post("http://posttestserver.com/post.php", parameters);
		String pageString = page.asString();
		assertTrue(pageString.contains(" Successfully dumped 2 post variables"));
	}
	
	@Test
	public void testDownloadToBuffer() {
		String wikipediaLogoUri = "http://upload.wikimedia.org/wikipedia/commons/6/63/Wikipedia-logo.png";
		byte [] logo = new MechanizeAgent().getToBuffer(wikipediaLogoUri);
		assertEquals(45283, logo.length);
	}
	
	@Test
	public void testDownloadToImage() {
		String wikipediaLogoUri = "http://upload.wikimedia.org/wikipedia/commons/6/63/Wikipedia-logo.png";
		BufferedImage image = new MechanizeAgent().getImage(wikipediaLogoUri);
		
		assertEquals(200, image.getWidth());
		assertEquals(200, image.getHeight());
	}

	@Test
	public void testDownloadToFile() {
		String wikipediaLogoUri = "http://upload.wikimedia.org/wikipedia/commons/6/63/Wikipedia-logo.png";
		String path = MechanizeAgentTest.class.getResource(".").getFile();

		File file = new File(path + "/" + "wikipedialogo.png");
		if(file.exists())
			file.delete();
		new MechanizeAgent().getToFile(wikipediaLogoUri, file);
		assertEquals(45283, file.length());
		file.delete();
	}
	
	
	@Test
	public void testDownloadPage() throws Exception {
		String wikipediaLogoUri = "http://upload.wikimedia.org/wikipedia/commons/6/63/Wikipedia-logo.png";
		File file = File.createTempFile("mechanize", ".png");
		file.delete();

		Page page = new MechanizeAgent().get(wikipediaLogoUri);
		
		assertTrue(page instanceof ContentPage);
		
		assertEquals("image/png", page.getContentType());
		page.saveTo(file);
		assertEquals(45283, file.length());
		file.delete();
		
		// in here my copied stream doesn't get the right bytes...
		
		page.saveTo(file);
		assertEquals(45283, file.length());
		file.delete();
	}
	
	@Test
	public void testPreAndPostRequestInterceptor() {
		Interceptor interceptor = new Interceptor();
		MechanizeAgent agent = new MechanizeAgent();
		agent.addInterceptor(interceptor);
		agent.get("http://wikipedia.org");
		assertEquals(2, interceptor.getCount());
	}
	
	public static class Interceptor implements RequestInterceptor, ResponseInterceptor {
		int count = 0;
		public void intercept(MechanizeAgent agent, HttpRequestBase request) {
			count ++;
		}
		
		public void intercept(MechanizeAgent agent, HttpResponse response, HttpRequestBase request) {
			count ++;
		}
		
		public int getCount() {
			return count;
		}
	}
}
