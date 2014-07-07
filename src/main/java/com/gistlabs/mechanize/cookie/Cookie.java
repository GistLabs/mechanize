/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.cookie;

import java.io.Serializable;

import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BasicClientCookie2;

/**
 * Representation of a cookie 
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class Cookie implements Serializable {

	private static final long serialVersionUID = -6982763905483623204L;

	private final org.apache.http.cookie.Cookie httpCookie; 
	
	public Cookie(org.apache.http.cookie.Cookie cookie) {
		this.httpCookie = cookie;
	}

	public Cookie(String name, String value) {
		this.httpCookie = new BasicClientCookie2(name, value);
	}
	
	//TODO needs additional test
	public Cookie(Cookie cookie) {
		try {
			this.httpCookie = (org.apache.http.cookie.Cookie)cookie.getHttpCookie().clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("Impossible Exception");
		}
	}

	public String getValue() {
		return httpCookie.getValue();
	}
	
	public String getName() {
		return httpCookie.getName();
	}
	
	public void setValue(String value) {
		((BasicClientCookie)httpCookie).setValue(value); 
	}

	public String getDomain() {
		return httpCookie.getDomain();
	}
	
	public BasicClientCookie getHttpCookie() {
		return (BasicClientCookie)httpCookie;
	}
	
	public boolean isRepresentingBasicClientCookie2() {
		return httpCookie instanceof BasicClientCookie2;
	}
	
	public BasicClientCookie2 getHttpCookieAsBasicClientCookie2() {
		return (BasicClientCookie2)httpCookie;
	}
	
	@Override
	public String toString() {
		return httpCookie.toString();
	}
}
