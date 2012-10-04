/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class Util {
	
	/** Returns the all elements matching any of the given tags (case-insensitive). */	
	public static Elements findElementsByTag(Element element, String ... tags) {
		List<Element> results = new ArrayList<Element>();
		
		Set<String> tagSet = new HashSet<String>();
		for(String tag : tags) 
			tagSet.add(tag.toLowerCase());
		filterElementsByTag(results, element, tagSet);
		return new Elements(results);
	}
	
	private static void filterElementsByTag(List<Element> results, Element element, Set<String> tagSet) {
		if(tagSet.contains(element.tag().getName().toLowerCase())) 
			results.add(element);
		
		for(Element child : element.children())
			filterElementsByTag(results, child, tagSet);
	}

	/** Returns the first element found with the given tag (or tag sequence separated by '/') or null. */
	public static Element findFirstByTag(Element element, String tag) {
		return findFirstByTag(element, tag.split("/"), 0);
	}
	
	private static Element findFirstByTag(Element current, String [] tags, int index) {
		if(index < tags.length) {
			Elements elements = current.getElementsByTag(tags[index]);
			for(Element element : elements) {
				Element result = findFirstByTag(element, tags, index + 1);
				if(result != null)
					return result;
			}
			return null;
		}
		else
			return current;
	}
	
	/** Returns the first header with the given name (case-insensitive) or null. */
	public static Header findHeader(HttpResponse response, String headerName) {
		Header[] headers = response.getHeaders(headerName);
		return headers.length > 0 ? headers[0] : null;
	}
	

	/** Returns a new File object from the given fileName and deleting the file if already exists. */
	public static File newFile(String fileName) {
		File file = new File(fileName);
		if(file.exists())
			file.delete();
		return file;
	}

	public static void copy(InputStream in, File outFile) throws IOException {
		copy(in, new FileOutputStream(outFile));
	}
	
	public static void copy(InputStream in, OutputStream out) throws IOException {
		try {
			byte[] buffer = new byte[1024*16];
			int len = in.read(buffer);
			while (len != -1) {
			    out.write(buffer, 0, len);
			    len = in.read(buffer);
			}			
		} finally {
			try {
				in.close();
			} catch(Throwable t) {}
			try {
				out.close();
			} catch(Throwable t) {}
		}
	}
}
