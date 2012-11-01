package com.gistlabs.mechanize.cache;

import static org.junit.Assert.*;

import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.message.BasicHeader;
import org.junit.Test;

public class HttpCacheFilterTest {

	@Test
	public void confirmHeaderElementParameterAssumptions() {
		Header header = new BasicHeader("Cache-Control", "max-age=3600,must-revalidate");
		assertEquals("Cache-Control", header.getName());
		HeaderElement[] elements = header.getElements();
		assertEquals(2, elements.length);

		assertEquals("max-age", elements[0].getName());
		assertEquals("3600", elements[0].getValue());
		assertEquals("must-revalidate", elements[1].getName());
		assertNull(elements[1].getValue());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void confirmDateUtil() throws DateParseException {
		Date date = DateUtils.parseDate("Sun, 09 Aug 2009 01:56:14 GMT");
		assertEquals("9 Aug 2009 01:56:14 GMT", date.toGMTString());
	}
}
