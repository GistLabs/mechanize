/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.interfaces.document;

import com.gistlabs.mechanize.interfaces.Link;

/**
 * Represents a link within a page.
 */
public interface NodeLink extends Link {

	public Node node();
}
