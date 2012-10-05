package com.gistlabs.mechanize;

/**
 * Describes a sequence of requests.
 * 
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public interface Sequence {

	public void run(MechanizeAgent agent);
}
