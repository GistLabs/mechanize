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
import java.util.LinkedHashSet;

import se.fishtank.css.selectors.specifier.PseudoContainsSpecifier;
import se.fishtank.css.util.Assert;


public class PseudoContainsSpecifierChecker<Node> extends AbstractChecker<Node> {

	/** The pseudo-class specifier to check against. */
	protected final PseudoContainsSpecifier specifier;

	/**
	 * Create a new instance.
	 * 
	 * @param specifier The pseudo-class specifier to check against.
	 */
	public PseudoContainsSpecifierChecker(final NodeHelper<Node> helper, final PseudoContainsSpecifier specifier) {
		super(helper);
		Assert.notNull(specifier, "specifier is null!");
		this.specifier = specifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Node> check(final Collection<Node> nodes) {
		Assert.notNull(nodes, "nodes is null!");
		this.nodes = nodes;

		this.result = new LinkedHashSet<Node>();

        String value = specifier.getValue();
        for (Node node : nodes) {
        	String content = helper.getValue(node);
        	if (content!=null && content.contains(value)) {
        		result.add(node);
        	}
        }
        
        return this.result;
	}
}
