package com.gistlabs.mechanize.html;

/**
 * Defines supported special attributes for html elements.
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public interface HtmlSpecialAttributes {

	/** A comma separated list of class names without white spaces (no trim needed). */
	String SPECIAL_ATTRIBUTE_CLASS_NAMES = "${classNames}";
	String SPECIAL_ATTRIBUTE_TAG_NAME = "${tagName}";
	String SPECIAL_ATTRIBUTE_INNER_HTML = "${innerHtml}";
	String SPECIAL_ATTRIBUTE_HTML = "${html}";
	String SPECIAL_ATTRIBUTE_TEXT = "${text}";
}
