/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.filters;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultMechanizeChainFilterTest {

	/**
	 * How many filters have been run?
	 */
	int counter=0;

	@Mock MechanizeFilter theEndMock;
	@InjectMocks DefaultMechanizeChainFilter defaultChainFilter;

	@Test
	public void testTailCall() {
		when(theEndMock.execute(any(HttpUriRequest.class), any(HttpContext.class))).thenReturn(mock(HttpResponse.class));

		assertNotNull(defaultChainFilter.execute(null, null));
	}

	@Test
	public void testOneFilterChainWithTailCall() {
		when(theEndMock.execute(any(HttpUriRequest.class), any(HttpContext.class))).thenReturn(mock(HttpResponse.class));

		defaultChainFilter.add(new MechanizeChainFilter() {
			@Override
			public HttpResponse execute(final HttpUriRequest request, final HttpContext context, final MechanizeFilter chain) {
				counter++;
				return chain.execute(request, context);
			}
		});

		assertNotNull(defaultChainFilter.execute(null, null));
		assertEquals(1, counter);
	}

	@Test
	public void testManyFilterChainWithTailCall() {
		when(theEndMock.execute(any(HttpUriRequest.class), any(HttpContext.class))).thenReturn(mock(HttpResponse.class));

		int length = 5;
		for (int i = 0; i < length ; i++)
			defaultChainFilter.add(new MechanizeChainFilter() {
				@Override
				public HttpResponse execute(final HttpUriRequest request, final HttpContext context, final MechanizeFilter chain) {
					counter++;
					return chain.execute(request, context);
				}
			});

		assertNotNull(defaultChainFilter.execute(null, null));
		assertEquals(length, counter);
	}

	@Test(expected=IllegalStateException.class)
	public void testOneFilterChainWithExceptionNoTailCall() {
		defaultChainFilter.add(new MechanizeChainFilter() {
			@Override
			public HttpResponse execute(final HttpUriRequest request, final HttpContext context, final MechanizeFilter chain) {
				throw new IllegalStateException();
			}
		});

		defaultChainFilter.execute(null, null);
	}
}
