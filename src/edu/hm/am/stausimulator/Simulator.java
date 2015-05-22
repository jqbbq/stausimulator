/*
 * 
 */
package edu.hm.am.stausimulator;

import edu.hm.am.stausimulator.model.Road;

// TODO: Auto-generated Javadoc
/**
 * The Class Simulator.
 */
public class Simulator {

	private SimulatorData data;

	private SimulatorModel model;

	/**
	 * Instantiates a new simulator.
	 */
	public Simulator() {
		data = new SimulatorData();
		model = new SimulatorModel();
	}

	public Road getRoad() {
		return model.getRoad();
	}

	public void nextStep() {
		model.nextStep();
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		new Simulator();
	}
}
