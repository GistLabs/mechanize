/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.gistlabs.mechanize.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;

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
    
}
