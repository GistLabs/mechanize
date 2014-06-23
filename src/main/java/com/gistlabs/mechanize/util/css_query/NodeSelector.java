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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import se.fishtank.css.selectors.NodeSelectorException;
import se.fishtank.css.selectors.Selector;
import se.fishtank.css.selectors.Specifier;
import se.fishtank.css.selectors.scanner.Scanner;
import se.fishtank.css.selectors.scanner.ScannerException;
import se.fishtank.css.selectors.specifier.AttributeSpecifier;
import se.fishtank.css.selectors.specifier.NegationSpecifier;
import se.fishtank.css.selectors.specifier.PseudoClassSpecifier;
import se.fishtank.css.selectors.specifier.PseudoContainsSpecifier;
import se.fishtank.css.selectors.specifier.PseudoNthSpecifier;
import se.fishtank.css.util.Assert;


public class NodeSelector<Node> {


	private final Node root;
	private final NodeHelper<Node> helper;

	public NodeSelector(final NodeHelper<Node> helper, final Node node) {
		Assert.notNull(node, "root is null!");
		Assert.notNull(helper, "helper is null!");
		this.root = node;
		this.helper = helper;
	}

	public Node find(final String selector) {
		List<Node> findAll = findAll(selector);

		if (findAll.size()>2) // too many results
			throw new RuntimeException(String.format("Too many resusts (%s) for selector: %s", findAll.size(), selector));
		else if (findAll.isEmpty())
			return null;
		else
			return findAll.iterator().next();
	}

	public List<Node> findAll(final String selector) {
		Assert.notNull(selector, "selectors is null!");
		List<List<Selector>> groups;
		try {
			Scanner scanner = new Scanner(selector);
			groups = scanner.scan();
		} catch (ScannerException e) {
			throw new RuntimeException(e);
		}

		Collection<Node> results = new LinkedHashSet<Node>();
		for (Collection<Selector> parts : groups) {
			Collection<Node> result;
			try {
				result = check(parts);
			} catch (NodeSelectorException e) {
				throw new RuntimeException(e);
			}
			if (!result.isEmpty())
				results.addAll(result);
		}

		return new ArrayList<Node>(results);
	}


	/**
	 * Check the list of selector <em>parts</em> and return a set of nodes with the result.
	 * 
	 * @param parts A list of selector <em>parts</em>.
	 * @return A set of nodes.
	 * @throws NodeSelectorException In case of an error.
	 */
	private Collection<Node> check(final Collection<Selector> parts) throws NodeSelectorException {
		Collection<Node> result = new LinkedHashSet<Node>();
		result.add(root);

		for (Selector selector : parts) {
			Checker<Node> checker = new TagChecker<Node>(helper, selector);
			result = checker.check(result);
			if (selector.hasSpecifiers())
				for (Specifier specifier : selector.getSpecifiers()) {
					switch (specifier.getType()) {
					case ATTRIBUTE:
						checker = new AttributeSpecifierChecker<Node>(helper, (AttributeSpecifier) specifier);
						break;
					case PSEUDO:
						if (specifier instanceof PseudoClassSpecifier)
							checker = new PseudoClassSpecifierChecker<Node>(helper, (PseudoClassSpecifier) specifier);
						else if (specifier instanceof PseudoNthSpecifier)
							checker = new PseudoNthSpecifierChecker<Node>(helper, (PseudoNthSpecifier) specifier);
						else if (specifier instanceof PseudoContainsSpecifier)
							checker = new PseudoContainsSpecifierChecker<Node>(helper, (PseudoContainsSpecifier) specifier);

						break;

					case NEGATION:
						final Collection<Node> negationNodes = checkNegationSpecifier((NegationSpecifier) specifier);
						checker = new Checker<Node>() {
							@Override
							public List<Node> check(final Collection<Node> nodes) {
								Collection<Node> set = new LinkedHashSet<Node>(nodes);
								set.removeAll(negationNodes);
								return new ArrayList<Node>(set);
							}
						};
						break;
					}

					result = checker.check(result);
					if (result.isEmpty())
						// Bail out early.
						return result;
				}
		}

		return result;
	}

	/**
	 * Check the {@link NegationSpecifier}.
	 * <p/>
	 * This method will add the {@link Selector} from the specifier in
	 * a list and invoke {@link #check(List)} with that list as the argument.
	 * 
	 * @param specifier The negation specifier.
	 * @return A set of nodes after invoking {@link #check(List)}.
	 * @throws NodeSelectorException In case of an error.
	 */
	private Collection<Node> checkNegationSpecifier(final NegationSpecifier specifier) throws NodeSelectorException {
		Collection<Selector> parts = new LinkedHashSet<Selector>(1);
		parts.add(specifier.getSelector());
		return check(parts);
	}

}
