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

import se.fishtank.css.selectors.specifier.AttributeSpecifier;
import se.fishtank.css.util.Assert;


public class AttributeSpecifierChecker<Node> extends AbstractChecker<Node> {

	/** The attribute specifier to check against. */
	protected final AttributeSpecifier specifier;

	public AttributeSpecifierChecker(final NodeHelper<Node> helper, final AttributeSpecifier specifier) {
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
		Collection<Node> result = new LinkedHashSet<Node>();
		for (Node node : nodes) {
			String name = specifier.getName();
			if (!helper.hasAttribute(node, name))
				continue;
			String attribute = helper.getAttribute(node, name);

			// It just have to be present.
			if (specifier.getValue() == null && attribute!=null) {
				result.add(node);
				continue;
			}

			String spec = specifier.getValue();
			switch (specifier.getMatch()) {
			case EXACT:
				if (attribute.equals(spec))
					result.add(node);

				break;
			case HYPHEN:
				if (attribute.equals(spec) || attribute.startsWith(spec + '-'))
					result.add(node);

				break;
			case PREFIX:
				if (attribute.startsWith(spec))
					result.add(node);

				break;
			case SUFFIX:
				if (attribute.endsWith(spec))
					result.add(node);

				break;
			case CONTAINS:
				if (attribute.contains(spec))
					result.add(node);

				break;
			case LIST:
				for (String v : attribute.split("\\s+"))
					if (v.equals(spec))
						result.add(node);

				break;
			}
		}

		return result;
	}

}