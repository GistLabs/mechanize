/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html.form;

import java.util.ArrayList;
import java.util.List;

import com.gistlabs.mechanize.document.node.Node;
import com.gistlabs.mechanize.util.css.CSSHelper;

/**
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 */
public class Select extends FormElement {

	private final boolean isMultiple;
	private final List<Option> options;
	
	public Select(Form form, Node node) {
		super(form, node);
		isMultiple = node.hasAttribute("multiple");
		options = new ArrayList<Option>();
		
		for(Node optionNode : node.findAll("option"))
			options.add(new Option(optionNode));
	}
	
	public boolean isMultiple() {
		return isMultiple;
	}
	
	/** Returns the option representing the given value or inner-HTML text. */
	public Option getOption(String valueOrText) {
		for(Option option : options) {
			if (option.matches(CSSHelper.contains(valueOrText))) {
				return option;
			}
		}
		return null;
		//return getOption(HtmlQueryBuilder.byValue(valueOrText).or.byInnerHtml(valueOrText));
	}
	
	/** Returns a new list containing all options in the order of occurence. */
	public List<Option> getOptions() {
		return new ArrayList<Option>(options);
	}
	
	@Override
	public void setValue(String value) {
		throw new UnsupportedOperationException("Value of hidden field may not be changed / set");
	}
	
	public class Option {

		private final Node node;
		private final String text;
		private final String value;
		
		private boolean isSelected;
		
		public Option(Node node) {
			this.node = node;
			text = node.getValue();
			value = node.hasAttribute("value") ? node.getAttribute("value") : text;
			isSelected = node.hasAttribute("selected");
		}

		public Node getNode() {
			return node;
		}
		
		public boolean matches(String csss) {
			return node.find(csss)!=null;
		}
		
		public boolean isSelected() {
			return isSelected;
		}
		
		public void setSelected(boolean isSelected) {
			if(isSelected && !isMultiple()) 
				for(Option option : options)
					option.unselect();
			this.isSelected = isSelected;
		}
		
		public void select() {
			setSelected(true);
		}
		
		public void unselect() {
			setSelected(false);
		}
		
		public String getText() {
			return text;
		}
		
		public String getValue() {
			return value;
		}
	}
}
