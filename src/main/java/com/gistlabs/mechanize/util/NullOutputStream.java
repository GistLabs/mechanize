package com.gistlabs.mechanize.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Do nothing with output, used to help consume content while CopyInputStream is used.
 * 
 * @author jheintz
 *
 */
public class NullOutputStream extends OutputStream {

	@Override
	public void write(int b) throws IOException {
	}
}
