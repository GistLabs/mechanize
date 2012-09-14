/*
 * Copyright (C) 2011 Gist Labs, LLC.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.gistlabs.mechanize;

import com.gistlabs.mechanize.exceptions.MechanizeException;

/**
 * Thrown in case of an io exception.
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class URISyntaxException extends MechanizeException {

	private static final long serialVersionUID = 1L;

	public URISyntaxException() {
		super();
	}

	public URISyntaxException(String message, Throwable cause) {
		super(message, cause);
	}

	public URISyntaxException(String message) {
		super(message);
	}

	public URISyntaxException(Throwable cause) {
		super(cause);
	}
}
