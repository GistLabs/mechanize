package com.gistlabs.mechanize.elements;

import java.util.Collection;
import java.util.List;

import com.gistlabs.mechanize.requestor.RequestBuilderFactory;

/**
 * Empty implementation... probably implement as wrapper/delgator for Element<..>
 * @author jheintz
 *
 * @param <Page>
 */
public class AbstractElement<Page extends RequestBuilderFactory<Page>> implements Element<Page> {

	@Override
	public Page getPage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttribute(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttribute(String key, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasAttribute(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<String> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Element<Page>> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

}
