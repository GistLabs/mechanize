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
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.gistlabs.mechanize.impl.DefaultResource;
import com.gistlabs.mechanize.impl.MechanizeAgent;
import com.gistlabs.mechanize.parameters.Parameters;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class MechanizeAgentIntTest extends MechanizeTestCase {
	static final String firefoxUserAgent = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.1) Gecko/20100122 firefox/3.6.1";

	protected Mechanize agent() {
		return new MechanizeAgent().setUserAgent(firefoxUserAgent);
	}

	@Test
	public void testPostMethod() {
		Mechanize agent = agent();
		Parameters parameters = new Parameters().add("param1", "value").add("param2", "value2");
		Resource page = agent.post("http://posttestserver.com/post.php", parameters);
		String pageString = page.asString();
		assertTrue(pageString.contains(" Successfully dumped 2 post variables"));
	}

	@Test
	public void testDownloadToImage() throws IOException {
		String wikipediaLogoUri = "http://upload.wikimedia.org/wikipedia/commons/6/63/Wikipedia-logo.png";
		BufferedImage image = ImageIO.read(agent().get(wikipediaLogoUri).getInputStream());

		assertEquals(200, image.getWidth());
		assertEquals(200, image.getHeight());
	}

	@Test
	public void testDownloadToFile() throws Exception {
		String wikipediaLogoUri = "http://upload.wikimedia.org/wikipedia/commons/6/63/Wikipedia-logo.png";
		File file = File.createTempFile("mechanize", ".png");
		file.delete();

		agent().get(wikipediaLogoUri).saveTo(file);
		assertEquals(45283, file.length());
		file.delete();
	}

	@Test
	public void testDownloadPage() throws Exception {
		String wikipediaLogoUri = "http://upload.wikimedia.org/wikipedia/commons/6/63/Wikipedia-logo.png";
		File file = File.createTempFile("mechanize", ".png");
		file.delete();

		Resource page = agent().get(wikipediaLogoUri);

		assertTrue(page instanceof DefaultResource);

		assertEquals("image/png", page.getContentType());
		page.saveTo(file);
		assertEquals(45283, file.length());
		file.delete();

		// in here my copied stream doesn't get the right bytes...

		page.saveTo(file);
		assertEquals(45283, file.length());
		file.delete();
	}
}
