/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Inspired by commons-io TeeInputStream, but without the dependency
 * @author John Heintz <john@gistlabs.com>
 *
 */
public class CopyInputStream extends InputStream {
    protected InputStream input;
    protected OutputStream copy;
    
    public CopyInputStream(InputStream input, OutputStream copy) {
        super();
        this.input = input;
        this.copy = copy;
    }

    public int read() throws IOException {
        int result = input.read();
        if (result != -1)
        	copy.write(result);
        return result;
    }

    public int available() throws IOException {
        return input.available();
    }

    public void close() throws IOException {
    	input.close();
    	copy.close();
    }

    public synchronized void mark(int readlimit) {
        input.mark(readlimit);
    }

    public boolean markSupported() {
        return input.markSupported();
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int result = input.read(b, off, len);
        if (result != -1)
        	copy.write(b, off, len);
        return result;
    }

    public int read(byte[] b) throws IOException {
        int result = input.read(b);
        if (result != -1)
        	copy.write(b, 0, result);
        return result;
    }

    public synchronized void reset() throws IOException {
        input.reset();
    }

    public long skip(long n) throws IOException {
        return input.skip(n);
    }

}