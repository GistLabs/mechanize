/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.debug;

import java.io.IOException;
import java.net.ProxySelector;

import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.cookie.Cookie;
import com.gistlabs.mechanize.document.Page;
import com.gistlabs.mechanize.form.Checkable;
import com.gistlabs.mechanize.form.Form;

public class Issue36Test {

	//@org.junit.Test
	public void testRedirect() throws IOException {
	    String username = "";
	    String password = "";
	    MechanizeAgent agent = new MechanizeAgent() {
	    	@Override
	    	protected AbstractHttpClient buildDefaultHttpClient() {
	    		AbstractHttpClient result = super.buildDefaultHttpClient();
	    		
	    		System.setProperty("http.proxyHost", "127.0.0.1");
	    		System.setProperty("http.proxyPort", "8888");	    		

	    		//	    		HttpHost proxy = new HttpHost("localhost", 8080);
	    		//	    		result.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	    		
	    		ProxySelectorRoutePlanner routePlanner = new ProxySelectorRoutePlanner(
	    		        result.getConnectionManager().getSchemeRegistry(),
	    		        ProxySelector.getDefault());  
	    		result.setRoutePlanner(routePlanner);
	    		
				return result;
	    	}
	    };

	    String manageKindleUrl = "http://www.amazon.com/gp/digital/fiona/manage/ref=gno_yam_myk";
	    Page signinPage = agent.get(manageKindleUrl);

	    debug(signinPage);

	    Form form = signinPage.forms().get(0);
	    form.get("email").setValue(username);
	    ((Checkable) form.get("ap_signin_existing_radio")).setChecked(true);
	    form.get("password").setValue(password);
	    Resource managePage = form.submit();

	    debug(managePage);

	}

	private void debug(Resource page) {
	    System.out.println("\n\n\n");
	    System.out.println("**** Page Headers ****");
	    System.out.println(page.getResponse().toString());
	    System.out.println("**** Page Cookies ****");
	    for (Cookie cookie : page.getAgent().cookies()) {
	        System.out.println(cookie.toString());
	    }
	    System.out.println("**** Page Body ****");
	    System.out.println(page.asString());
	}
}
