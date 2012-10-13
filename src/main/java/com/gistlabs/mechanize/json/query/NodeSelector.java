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
import se.fishtank.css.selectors.specifier.PseudoNthSpecifier;
import se.fishtank.css.util.Assert;


public class NodeSelector<JsonNode> {
	

	private final JsonNode root;
	private final NodeHelper<JsonNode> helper;

	public NodeSelector(NodeHelper<JsonNode> helper, JsonNode node) {
		this.root = node;
		this.helper = helper;
	}

	public JsonNode find(String selector) {
		List<JsonNode> findAll = findAll(selector);
		
		if (findAll.size()>2) // too many results
			throw new RuntimeException(String.format("Too many resusts (%s) for selector: %s", findAll.size(), selector));
		else if (findAll.isEmpty())
			return null;
		else
			return findAll.iterator().next();
	}
	
	public List<JsonNode> findAll(String selector) {
        Assert.notNull(selector, "selectors is null!");
        List<List<Selector>> groups;
        try {
            Scanner scanner = new Scanner(selector);
            groups = scanner.scan();
        } catch (ScannerException e) {
            throw new RuntimeException(e);
        }

        Collection<JsonNode> results = new LinkedHashSet<JsonNode>();
        for (Collection<Selector> parts : groups) {
            Collection<JsonNode> result;
			try {
				result = check(parts);
			} catch (NodeSelectorException e) {
				throw new RuntimeException(e);
			}
            if (!result.isEmpty()) {
                results.addAll(result);
            }
        }

        return new ArrayList<JsonNode>(results);
	}

    
    /**
     * Check the list of selector <em>parts</em> and return a set of nodes with the result.
     * 
     * @param parts A list of selector <em>parts</em>.
     * @return A set of nodes.
     * @throws NodeSelectorException In case of an error.
     */
    private Collection<JsonNode> check(Collection<Selector> parts) throws NodeSelectorException {
        Collection<JsonNode> result = new LinkedHashSet<JsonNode>();
        result.add(root);
        
        for (Selector selector : parts) {
            Matcher<JsonNode> checker = new TagMatcher<JsonNode>(helper, selector);
            result = checker.match(result);
            if (selector.hasSpecifiers()) {
                for (Specifier specifier : selector.getSpecifiers()) {
                    switch (specifier.getType()) {
                    case ATTRIBUTE:
                        checker = new AttributeSpecifierMatcher<JsonNode>(helper, (AttributeSpecifier) specifier);
                        break;
                    case PSEUDO:
                        if (specifier instanceof PseudoClassSpecifier) {
                            checker = new PseudoClassSpecifierMatcher<JsonNode>(helper, (PseudoClassSpecifier) specifier);
                        } else if (specifier instanceof PseudoNthSpecifier) {
                            checker = new PseudoNthSpecifierMatcher<JsonNode>(helper, (PseudoNthSpecifier) specifier);
                        }
                        
                        break;
                        
                    case NEGATION:
                        final Collection<JsonNode> negationNodes = checkNegationSpecifier((NegationSpecifier) specifier);
                        checker = new Matcher<JsonNode>() {
                            @Override
                            public List<JsonNode> match(Collection<JsonNode> nodes) {
                                Collection<JsonNode> set = new LinkedHashSet<JsonNode>(nodes);
                                set.removeAll(negationNodes);
                                return new ArrayList<JsonNode>(set);
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
    private Collection<JsonNode> checkNegationSpecifier(NegationSpecifier specifier) throws NodeSelectorException {
        Collection<Selector> parts = new LinkedHashSet<Selector>(1);
        parts.add(specifier.getSelector());
        return check(parts);
    }
    
}
