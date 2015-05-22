/*
 * 
 */
package edu.hm.am.stausimulator;

import java.util.List;
import java.util.Observable;

import edu.hm.am.stausimulator.model.Road;

// TODO: Auto-generated Javadoc
/**
 * The Class Simulator.
 */
public class Simulator extends Observable {

	public static final String RESET = "reset";

	private Run model;

	private Runner runner;

	/**
	 * Instantiates a new simulator.
	 */
	public Simulator() {
		init();
	}

	public List<Road> getRoads() {
		return model.getRoads();
	}

	public Road getRoad(int road) {
		return model.getRoad(road);
	}

	public int getStep() {
		return model.getStep();
	}

	public Run getModel() {
		return model;
	}

	public Runner getRunner() {
		return runner;
	}

	public void nextStep() {
		model.nextStep();
	}

	public boolean reset() {
		return init();
	}

	private boolean init() {
		model = new Run();
		runner = new Runner("Main", Configuration.getProperty(Property.INTERVAL).intValue());

		runner.addRunnable(new Runnable() {
			@Override
			public void run() {
				nextStep();
			}
		});
		runner.start();
		return true;
	}

}
