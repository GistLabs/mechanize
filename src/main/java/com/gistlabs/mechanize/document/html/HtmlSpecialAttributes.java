/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.html;

import com.gistlabs.mechanize.SpecialAttributes;

/**
 * Defines supported special attributes for html elements.
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public interface HtmlSpecialAttributes extends SpecialAttributes {

	/** A comma separated list of class names without white spaces (no trim needed). */
	String SPECIAL_ATTRIBUTE_CLASS_NAMES = "${classNames}";
	String SPECIAL_ATTRIBUTE_TAG_NAME = SPECIAL_ATTRIBUTE_NODE_NAME;
	String SPECIAL_ATTRIBUTE_INNER_HTML = "${innerHtml}";
	String SPECIAL_ATTRIBUTE_HTML = "${html}";
	String SPECIAL_ATTRIBUTE_TEXT = SPECIAL_ATTRIBUTE_NODE_VALUE;
}
