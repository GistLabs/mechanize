package com.gistlabs.mechanize.json.query;

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
import se.fishtank.css.util.Assert;

import com.gistlabs.mechanize.json.Node;
import com.gistlabs.mechanize.json.query.matchers.AttributeSpecifierMatcher;
import com.gistlabs.mechanize.json.query.matchers.PseudoClassSpecifierMatcher;
import com.gistlabs.mechanize.json.query.matchers.TagMatcher;

public class NodeSelector {
	

	private final Node root;

	public NodeSelector(Node node) {
		this.root = node;
	}

	public List<Node> findAll(String selectors) {
        Assert.notNull(selectors, "selectors is null!");
        List<List<Selector>> groups;
        try {
            Scanner scanner = new Scanner(selectors);
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
            if (!result.isEmpty()) {
                results.addAll(result);
            }
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
    private Collection<Node> check(Collection<Selector> parts) throws NodeSelectorException {
        Collection<Node> result = new LinkedHashSet<Node>();
        result.add(root);
        
        for (Selector selector : parts) {
            Matcher<Node> checker = new TagMatcher(selector);
            result = checker.match(result);
            if (selector.hasSpecifiers()) {
                for (Specifier specifier : selector.getSpecifiers()) {
                    switch (specifier.getType()) {
                    case ATTRIBUTE:
                        checker = new AttributeSpecifierMatcher((AttributeSpecifier) specifier);
                        break;
                    case PSEUDO:
                        if (specifier instanceof PseudoClassSpecifier) {
                            checker = new PseudoClassSpecifierMatcher((PseudoClassSpecifier) specifier);
//                        } else if (specifier instanceof PseudoNthSpecifier) {
//                            checker = new PseudoNthSpecifierChecker((PseudoNthSpecifier) specifier);
                        }
                        
                        break;
                        
                    case NEGATION:
                        final Collection<Node> negationNodes = checkNegationSpecifier((NegationSpecifier) specifier);
                        checker = new Matcher<Node>() {
                            @Override
                            public List<Node> match(Collection<Node> nodes) {
                                Collection<Node> set = new LinkedHashSet<Node>(nodes);
                                set.removeAll(negationNodes);
                                return new ArrayList<Node>(set);
                            }
                        };
                        break;
                    }
                    
                    result = checker.match(result);
                    if (result.isEmpty()) {
                        // Bail out early.
                        return result;
                    }
                }
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
    private Collection<Node> checkNegationSpecifier(NegationSpecifier specifier) throws NodeSelectorException {
        Collection<Selector> parts = new LinkedHashSet<Selector>(1);
        parts.add(specifier.getSelector());
        return check(parts);
    }
    
}
