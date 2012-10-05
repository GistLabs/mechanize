package com.gistlabs.mechanize.util;

import java.util.Collection;
import java.util.HashSet;

public class Collections {
	public static <T> Collection<T> collection(T... entries) {
		HashSet<T> result = new HashSet<T>(entries.length);
		for (T entry : entries) {
			result.add(entry);
		}
		return result;
	}
}
