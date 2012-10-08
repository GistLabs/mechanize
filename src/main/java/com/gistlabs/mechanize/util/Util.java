/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.util;

import java.io.*;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class Util {
	
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
