package com.gistlabs.mechanize.sequence;

import com.gistlabs.mechanize.MechanizeAgent;

/**
 * Base implementation stub for a Sequence. The default name of the sequence it the canonical name of the 
 * implementation class and the agent parameter of run has moved to a protected field to avoid passing the
 * agent along in the run method and sub-methods.
 * 
 * <p>Note: After run() execution the agent is reset to null to avoid memory leaks. </p>  
 * 
 * @author Martin Kersten <Martin.Kersten.mk@gmail.com>
 */
public abstract class AbstractSequence implements Sequence {

	protected MechanizeAgent agent;
	
	public String getName() {
		return getClass().getCanonicalName();
	}
	
	public void run(MechanizeAgent agent) {
		this.agent = agent;
		run();
		this.agent = null;
	}
	
	public MechanizeAgent getAgent() {
		return agent;
	}
	
	abstract protected void run();
}
