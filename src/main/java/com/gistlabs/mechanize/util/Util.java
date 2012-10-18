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
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class Util {
	
	public static <T> List<T> newEmptyList() {
		return new ArrayList<T>();
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
