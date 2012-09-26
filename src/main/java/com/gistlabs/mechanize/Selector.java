/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

/**
 * Used to specify which attributes (and non attributes) are relevant for finding proper elements. 
 * <p>The ALL selector represents all the available sectors and not(!) all available attributes.</p>  
 *  
 * @author Martin Kersten<Martin.Kersten.mk@gmail.com>
 * @version 1.0
 * @since 2012-09-12
 */
public class Selector {
	private final int mask;
	
	public static final Selector ANY = new Selector(1<<0);
	public static final Selector NAME = new Selector(1<<1);
	public static final Selector ID = new Selector(1<<2);
	public static final Selector ID_OR_CLASZ = new Selector((1<<2) + (1<<4));
	public static final Selector NAME_OR_ID = new Selector((1<<1) + (1<<2));
	public static final Selector TAG = new Selector(1<<3);
	public static final Selector CLASZ = new Selector(1<<4);
	public static final Selector ACTION = new Selector(1<<5);
	public static final Selector HREF = new Selector(1<<6);
	public static final Selector SRC = new Selector(1<<7);
	public static final Selector TITLE = new Selector(1<<8);
	public static final Selector WIDTH = new Selector(1<<9);
	public static final Selector HEIGHT = new Selector(1<<10);
	public static final Selector TYPE = new Selector(1<<11);
	public static final Selector VALUE = new Selector(1<<12);
	public static final Selector TEXT = new Selector(1<<13);
	public static final Selector INNER_HTML = new Selector(1<<14);
	public static final Selector HTML = new Selector(1<<15);
	
	private Selector(int mask) {
		this.mask = mask;
	}
	
	public boolean includesAny() {
		return (mask & ANY.mask) != 0;
	}

	public boolean includesName() {
		return (mask & NAME.mask) != 0;
	}
	
	public boolean includesId() {
		return (mask & ID.mask) != 0;
	}
	
	public boolean includesClass() {
		return (mask & CLASZ.mask) != 0;
	}
	
	public boolean includesTag() {
		return (mask & TAG.mask) != 0;
	}
	
	public boolean includesAction() {
		return (mask & ACTION.mask) != 0;
	}
	
	public boolean includesHRef() {
		return (mask & HREF.mask) != 0;
	}
	
	public boolean includesSrc() {
		return (mask & SRC.mask) != 0;
	}
	
	public boolean includesTitle() {
		return (mask & TITLE.mask) != 0;
	}

	public boolean includesWidth() {
		return (mask & WIDTH.mask) != 0;
	}
	public boolean includesHeight() {
		return (mask & HEIGHT.mask) != 0;
	}

	public boolean includesValue() {
		return (mask & VALUE.mask) != 0;
	}
	
	public boolean includesType() {
		return (mask & TYPE.mask) != 0;
	}

	public boolean includesText() {
		return (mask & TEXT.mask) != 0;
	}

	public boolean includesInnerHtml() {
		return (mask & INNER_HTML.mask) != 0;
	}

	public boolean includesHtml() {
		return (mask & HTML.mask) != 0;
	}
	
	public String toString() {
		StringBuilder toString = new StringBuilder();
		toString.append("[");
		append(toString, "any", includesAny()); 
		append(toString, "name", includesName()); 
		append(toString, "id", includesId()); 
		append(toString, "class", includesClass()); 
		append(toString, "tag", includesTag()); 
		append(toString, "action", includesAction()); 
		append(toString, "href", includesHRef()); 
		append(toString, "src", includesSrc()); 
		append(toString, "title", includesTitle()); 
		append(toString, "width", includesWidth()); 
		append(toString, "height", includesHeight()); 
		append(toString, "value", includesValue()); 
		append(toString, "type", includesType()); 
		append(toString, "text", includesText()); 
		append(toString, "innerHtml", includesInnerHtml());
		append(toString, "html", includesHtml());
		toString.append("]");
		return toString.toString();
	}

	private void append(StringBuilder toString, String string, boolean toAdd) {
		if(toAdd) {
			if(toString.length() > 1)
				toString.append(",");
			toString.append(string);
		}
	}
}
