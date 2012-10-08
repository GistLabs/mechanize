/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.sequence;

import com.gistlabs.mechanize.MechanizeAgent;

/**
 * Describes a named sequence of actions using an agent.
 * 
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public interface Sequence {

	public String getName();
	
	public void run(MechanizeAgent agent);
}
