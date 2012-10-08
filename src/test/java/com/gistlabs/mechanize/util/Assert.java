package com.gistlabs.mechanize.util;

/**
 * Provides Assertion methods for parameters and alike.
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public class Assert {
	public static void notNull(Object object) {
		if(object == null)
			throw new NullPointerException();
	}

	public static void notNull(Object object, String message) {
		if(object == null)
			throw new NullPointerException(message);
	}
}
