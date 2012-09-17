/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Interceptor notified after a request was send and a response was received making it possible
 * to inspect and alter the response object and also do various checks depending on the response 
 * and request object. 
 * 
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public interface ResponseInterceptor extends Interceptor {
	void intercept(MechanizeAgent agent, HttpResponse response, HttpRequestBase request);
}
