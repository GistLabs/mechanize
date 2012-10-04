package com.gistlabs.mechanize.util;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CopyInputStreamTest {
	URL logo;
	private int resourceSize = 26771;

	@Before
	public void setUp() throws Exception {
		logo = getClass().getResource("logo.png");
		assertNotNull(logo);
		
		File file = new File(logo.getFile());
		assertTrue(file.exists());
		assertEquals(resourceSize, getLength(file));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDirectDirectStreams() throws IOException {
		File temp = createTempFile();
		Util.copy(logo.openStream(), temp);
		assertEquals(resourceSize, getLength(temp));
	}

	@Test
	public void testDirectCopyStream() throws IOException {
		File temp = createTempFile();
		File temp2 = createTempFile();
		
		ByteArrayOutputStream copy = new ByteArrayOutputStream(resourceSize);
		CopyInputStream copyIS = new CopyInputStream(logo.openStream(), copy);
		
		Util.copy(copyIS, temp);
		assertEquals(resourceSize, getLength(temp));
		assertEquals(resourceSize, copy.size());
		
		ByteArrayInputStream newIS = new ByteArrayInputStream(copy.toByteArray());
		Util.copy(newIS, temp2);
		assertEquals(resourceSize, getLength(temp2));
	}

	protected int getLength(File file) {
		return (int)file.length();
	}

	protected File createTempFile() throws IOException {
		File temp = File.createTempFile("mechanize", ".png");
		temp.delete();
		return temp;
	}
}
