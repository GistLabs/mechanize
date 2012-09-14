/*
 * Copyright (C) 2011 Gist Labs, LLC.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.gistlabs.mechanize;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * Interceptor being notified before a request is actually send making it possible to inspect and alter the request
 * by adding or removing header entries, changing the destination URL and modifying various aspects of the underlying
 * HTTP protocol.
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public interface RequestInterceptor extends Interceptor {
	void intercept(MechanizeAgent agent, HttpRequestBase request);
}
