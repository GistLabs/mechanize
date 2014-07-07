/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.exceptions;

/**
 * Thrown in case of an IOException.
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class MechanizeClientProtocolException extends MechanizeException {

	private static final long serialVersionUID = 1L;

	public MechanizeClientProtocolException() {
		super();
	}

	public MechanizeClientProtocolException(String message, Throwable cause) {
		super(message, cause);
	}

	public MechanizeClientProtocolException(String message) {
		super(message);
	}

	public MechanizeClientProtocolException(Throwable cause) {
		super(cause);
	}
}
