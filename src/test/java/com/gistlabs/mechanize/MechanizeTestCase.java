/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.After;

import com.gistlabs.mechanize.MechanizeMock.PageRequest;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class MechanizeTestCase {
	protected MechanizeMock agent = new MechanizeMock();
	protected boolean doAfterTest = true;

	public void disableAfterTest() {
		doAfterTest = false;
	}

	@After
	public void afterTest() {
		if(doAfterTest) {
			PageRequest next = agent.nextUnexecutedPageRequest();
			if(next != null)
				Assert.fail("Unexecuted page request: " + next.toString());
		}
	}

	protected String newHtml(final String title, final String bodyHtml) {
		return "<html><head><title>" + title + "</title></head><body>" + bodyHtml + "</body></html>";
	}

	public List<NameValuePair> parameter(final String name, final String value) {
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		result.add(new BasicNameValuePair(name, value));
		return result;
	}
}
