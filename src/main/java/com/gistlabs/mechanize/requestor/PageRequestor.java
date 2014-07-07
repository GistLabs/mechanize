/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.requestor;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public interface PageRequestor<Resource> {
	public <T extends Resource> T request(HttpRequestBase request);
}
