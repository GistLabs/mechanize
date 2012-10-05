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
