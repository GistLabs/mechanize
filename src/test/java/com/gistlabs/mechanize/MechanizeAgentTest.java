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
	
	@Test
	public void testPostMethod() {
		MechanizeAgent agent = new MechanizeAgent();
		Parameters parameters = new Parameters().add("param1", "value").add("param2", "value2");
		Page page = agent.post("http://posttestserver.com/post.php", parameters);
		System.out.println(page.getDocument().toString());
		assertTrue(page.getDocument().toString().contains(" Successfully dumped 2 post variables"));
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
