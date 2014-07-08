/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.integration.test;

import java.io.IOException;
import java.net.ProxySelector;

import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;

import com.gistlabs.mechanize.Mechanize;
import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.cookie.Cookie;
import com.gistlabs.mechanize.document.AbstractDocument;
import com.gistlabs.mechanize.document.html.form.Checkable;
import com.gistlabs.mechanize.document.html.form.Form;
import com.gistlabs.mechanize.impl.MechanizeAgent;

public class Issue36Test {

	//@org.junit.Test
	public void testRedirect() throws IOException {
		String username = "";
		String password = "";
		Mechanize agent = new MechanizeAgent(buildClient());

		String manageKindleUrl = "http://www.amazon.com/gp/digital/fiona/manage/ref=gno_yam_myk";
		AbstractDocument signinPage = agent.get(manageKindleUrl);

		debug(signinPage);

		Form form = signinPage.forms().get(0);
		form.get("email").setValue(username);
		((Checkable) form.get("ap_signin_existing_radio")).setChecked(true);
		form.get("password").setValue(password);
		Resource managePage = form.submit();

		debug(managePage);

	}

	private AbstractHttpClient buildClient() {
		AbstractHttpClient result = MechanizeAgent.buildDefaultHttpClient();

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
	private void debug(final Resource page) {
		System.out.println("\n\n\n");
		System.out.println("**** Page Headers ****");
		System.out.println(page.getResponse().toString());
		System.out.println("**** Page Cookies ****");
		for (Cookie cookie : page.getAgent().cookies())
			System.out.println(cookie.toString());
		System.out.println("**** Page Body ****");
		System.out.println(page.asString());
	}
}
