/**
 * Modification from original by Gist Labs, LLC (http://gistlabs.com)
 * 
 * Copyright (c) 2009-2012, Christer Sandberg
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.gistlabs.mechanize.util.css_query;

import java.util.Collection;

import se.fishtank.css.util.Assert;


public abstract class AbstractChecker<Node> implements Checker<Node> {

	protected final NodeHelper<Node> helper;

	/** The set of nodes to check. */
	protected Collection<Node> nodes;

	/** The result of the checks. */
	protected Collection<Node> result;

	public AbstractChecker(final NodeHelper<Node> helper) {
		Assert.notNull(helper, "helper is null!");
		this.helper = helper;
	}
}
