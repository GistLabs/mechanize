/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.cookie;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.AbstractHttpClient;

/**
 *  Collection of the current available cookies. 
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class Cookies implements Iterable<Cookie> {
	private final AbstractHttpClient client;
	
	private final WeakHashMap<org.apache.http.cookie.Cookie, Cookie> cookieRepresentationCache = new WeakHashMap<org.apache.http.cookie.Cookie, Cookie>();

	public Cookies(AbstractHttpClient client) {
		this.client = client;
	}

	private CookieStore getStore() {
		return client.getCookieStore();
	}
	
	private Cookie getCookie(org.apache.http.cookie.Cookie cookie) {
		Cookie cookieRepresentation = cookieRepresentationCache.get(cookie);
		if(cookieRepresentation == null) {
			cookieRepresentation = new Cookie(cookie);
			cookieRepresentationCache.put(cookie, cookieRepresentation);
		}
		return cookieRepresentation;
	}
	
	/** Returns the cookie with the given name and for the given domain or null. */
	public Cookie get(String name, String domain) {
		for(Cookie cookie : this) {
			if(cookie.getName().equals(name) && cookie.getDomain().equals(domain))
				return cookie; 
		}
		return null;
	}
	
	/** Returns a list of all cookies currently managed by the underlying http client. */ 
	public List<Cookie> getAll() {
		List<Cookie> cookies = new ArrayList<Cookie>();
		for(org.apache.http.cookie.Cookie cookie : getStore().getCookies()) 
			cookies.add(getCookie(cookie));
		return cookies;
	}
	
	/** Returns the number of currently existing cookies. */ 
	public int getCount() {
		return getStore().getCookies().size();
	}
	
	public Cookie addNewCookie(String name, String value, String domain) {
		Cookie cookie = new Cookie(name, value);
		cookie.getHttpCookie().setDomain(domain);
		cookieRepresentationCache.put(cookie.getHttpCookie(), cookie);
		getStore().addCookie(cookie.getHttpCookie());
		return cookie;
	}

	/** Adds all cookies by actually cloning them. */
	public void addAllCloned(List<Cookie> cookies) {
		for(Cookie cookie : cookies) {
			Cookie clone = new Cookie(cookie);
			getStore().addCookie(clone.getHttpCookie());
		}
	}
	
	/** Removes all current managed cookies. */
	public void removeAll() {
		getStore().clear();
	}
	
	/** Removes the current cookie by changing the expired date and force the store to remove all expired for a given date. The date will be set to a time of 1970. */
	public void remove(Cookie cookie) {
		cookie.getHttpCookie().setExpiryDate(new Date(0));
		cookieRepresentationCache.remove(cookie.getHttpCookie());
		getStore().clearExpired(new Date(1));
	}
	
	public Iterator<Cookie> iterator() {
		return getAll().iterator();
	}
	
	/** Writes all cookies to System.out including all the outer HTML. */
	public void dumpAllToSystemOut() {	
		dumpToSystemOut(getAll());
	}
	
	public void dumpToSystemOut(List<Cookie> cookies) {
		for(Cookie cookie : cookies) 
			System.out.println("cookie: " + cookie);
	}
}
