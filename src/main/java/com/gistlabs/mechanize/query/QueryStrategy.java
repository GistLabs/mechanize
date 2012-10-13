package com.gistlabs.mechanize.query;

import java.util.List;

/**
 * Describes a query matching strategy for extracting attributes form a given object.
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public interface QueryStrategy {
	/** A comma separated list of class names without white spaces (no trim needed). */
	String SPECIAL_ATTRIBUTE_CLASS_NAMES = "${classNames}";
	String SPECIAL_ATTRIBUTE_TAG_NAME = "${nodeName}";
	String SPECIAL_ATTRIBUTE_INNER_HTML = "${innerHtml}";
	String SPECIAL_ATTRIBUTE_HTML = "${html}";
	String SPECIAL_ATTRIBUTE_TEXT = "${value}";
	
	/** Returns the value of the object or null while attribute can also refer to special attributes. */
	String getAttributeValue(Object object, String attributeKey);
	
	/** Returns true if the given attribute is a multiple value attribute listing values by comma separated list. */
	boolean isMultipleValueAttribute(Object object, String attributeKey);

	/** Returns all attribute names of the object. This includes also supported attributes. */
	List<String> getAttributeNames(Object object);
}
