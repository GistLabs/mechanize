/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
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
