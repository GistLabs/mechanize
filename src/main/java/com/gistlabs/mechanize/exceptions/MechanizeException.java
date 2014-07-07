/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.exceptions;

/**
 * General purpose exception.
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class MechanizeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MechanizeException() {
		super();
	}

	public MechanizeException(String message, Throwable cause) {
		super(message, cause);
	}

	public MechanizeException(String message) {
		super(message);
	}

	public MechanizeException(Throwable cause) {
		super(cause);
	}
}
