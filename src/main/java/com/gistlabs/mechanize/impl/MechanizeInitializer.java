/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gistlabs.mechanize.ResourceFactory;
import com.gistlabs.mechanize.exceptions.MechanizeExceptionFactory;

/**
 * <p>This will initialize Mechanize (at static load time) with Page types identified by content matches.</p>
 * 
 * <p>Mechanize supports different Page types, mapped by ContentType. The system property "mechanize.page.factories" is a
 * comma-separated list of classnames for the default Page types. Today this is com.gistlabs.mechanize.HtmlPage and
 * com.gistlabs.mechanize.ContentPage. Modify this property ONLY if you want to change the default loaded Page types.
 * The system property "mechanize.page.factories.ext" is a also loaded, and provides the typical way for framework extenders
 * to add custom content types, or MechanizeAgent.registerPageType(Class).</p>

 * 
 * @author jheintz
 *
 */
class MechanizeInitializer {
	private static final Log log = LogFactory.getLog(MechanizeInitializer.class);

	public static final String MECHANIZE_PAGE_FACTORIES = "mechanize.page.factories";
	public static final String MECHANIZE_PAGE_FACTORIES_EXT = "mechanize.page.factories.ext";
	public static final String DEFAULT_FACTORIES = "com.gistlabs.mechanize.document.html.HtmlDocumentFactory, com.gistlabs.mechanize.impl.DefaultResourceFactory, com.gistlabs.mechanize.document.json.JsonDocumentFactory";


	static void initialize() {
		processFactoriesClassNames(getClassNames(MECHANIZE_PAGE_FACTORIES, DEFAULT_FACTORIES));
		processFactoriesClassNames(getClassNames(MECHANIZE_PAGE_FACTORIES_EXT, ""));
		try {
			loadProperties();
		} catch (Exception e) {
			log.fatal("Failed to load /mechanize.properties file", e);
			throw MechanizeExceptionFactory.newException(e);
		}
	}

	protected static void loadProperties() throws Exception {
		Properties properties = new Properties();
		properties.load(MechanizeInitializer.class.getResourceAsStream("/mechanize.properties"));
		MechanizeAgent.setVersion(properties.getProperty("mechanize.version"));
	}

	protected static void processFactoriesClassNames(final List<String> factoryClassNames) {
		for (String factoryClassName : factoryClassNames)
			try {
				@SuppressWarnings("unchecked")
				Class<ResourceFactory> pageFactoryClass = (Class<ResourceFactory>) Class.forName(factoryClassName);
				ResourceFactory pageFactory = pageFactoryClass.newInstance();
				MechanizeAgent.registerFactory(pageFactory);
			} catch (Exception e) {
				// TODO add logging...
				throw MechanizeExceptionFactory.newInitializationException(e);
			}
	}

	static List<String> getClassNames(final String forSystemProperty, final String orDefaultValue) {
		List<String> result = new ArrayList<String>();
		String propertyValue = System.getProperty(forSystemProperty);

		if (propertyValue==null || "".equals(propertyValue))
			propertyValue = orDefaultValue;

		StringTokenizer tokenizer = new StringTokenizer(propertyValue, ",");
		while(tokenizer.hasMoreTokens())
			result.add(tokenizer.nextToken().trim());

		return result;
	}
}
