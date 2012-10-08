/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.gistlabs.mechanize.html;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This file inspired by org.jsoup.helper.DataUtil.getCharsetFromContentType(String) method.
 * @author John Heintz <john@gistlabs.com>
 */
public class JsoupDataUtil {
    private static final Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*\"?([^\\s;\"]*)");
    static final String defaultCharset = "UTF-8"; // used if not found in header or meta charset

    /**
     * Parse out a charset from a content type header.
     * @param header e.g. "text/html; charset=EUC-JP"
     * @return "EUC-JP", or null if not found. Charset is trimmed and uppercased.
     */
    public static String getCharsetFromContentType(Header header) {
        if (header == null || header.getValue()==null || "".equals(header.getValue())) return null;
        Matcher m = charsetPattern.matcher(header.getValue());
        if (m.find()) {
            return m.group(1).trim().toUpperCase();
        }
        return null;
    }
	
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
    
}
