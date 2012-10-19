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

import se.fishtank.css.selectors.dom.internal.NodeTraversalChecker;
import se.fishtank.css.selectors.specifier.PseudoNthSpecifier;
import se.fishtank.css.util.Assert;

import com.gistlabs.mechanize.util.css_query.NodeHelper.Index;

/**
 * A {@link NodeTraversalChecker} that check if a node matches
 * the {@code nth-*} {@linkplain PseudoNthSpecifier pseudo-class specifier} set.
 * 
 * @author Christer Sandberg
 */
public class PseudoNthSpecifierChecker<Node> extends AbstractChecker<Node> {

	/** The {@code nth-*} pseudo-class specifier to check against. */
	private final PseudoNthSpecifier specifier;

	/**
	 * Create a new instance.
	 * 
	 * @param specifier The {@code nth-*} pseudo-class specifier to check against.
	 */
	public PseudoNthSpecifierChecker(final NodeHelper<Node> helper, final PseudoNthSpecifier specifier) {
		super(helper);
		Assert.notNull(specifier, "specifier is null!");
		this.specifier = specifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Node> check(final Collection<Node> nodes)  {
		Assert.notNull(nodes, "nodes is null!");
		this.nodes = nodes;
		result = new LinkedHashSet<Node>();
		String value = specifier.getValue();

		if ("nth-child".equals(value))
			addNthChild(false);
		else if ("nth-last-child".equals(value))
			addNthLastChild(false);
		else if ("nth-of-type".equals(value))
			addNthChild(true);
		else if ("nth-last-of-type".equals(value))
			addNthLastChild(false);
		else
			throw new RuntimeException("Unknown pseudo nth class: " + value);

		return result;
	}

	/**
	 * Add the {@code :nth-child} elements.
	 * 
	 * @see <a href="http://www.w3.org/TR/css3-selectors/#nth-child-pseudo"><code>:nth-child</code> pseudo-class</a>
	 */
	private void addNthChild(final boolean byType) {
		for (Node node : nodes) {
			Index index = helper.getIndexInParent(node, byType);

			if (specifier.isMatch(index.index+1))
				result.add(node);
		}
	}

	/**
	 * Add {@code :nth-last-child} elements.
	 * 
	 * @see <a href="http://www.w3.org/TR/css3-selectors/#nth-last-child-pseudo"><code>:nth-last-child</code> pseudo-class</a>
	 */
	private void addNthLastChild(final boolean byType) {
		for (Node node : nodes) {
			Index index = helper.getIndexInParent(node, byType);

			if (specifier.isMatch(index.size - index.index+1))
				result.add(node);
		}
	}
}
