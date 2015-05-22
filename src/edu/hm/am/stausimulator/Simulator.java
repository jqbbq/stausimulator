/*
 * 
 */
package edu.hm.am.stausimulator;

import java.util.List;

import edu.hm.am.stausimulator.model.Road;


// TODO: Auto-generated Javadoc
/**
 * The Class Simulator.
 */
public class Simulator {

	private Run model;

	/**
	 * Instantiates a new simulator.
	 */
	public Simulator() {
		init();
	}

	public List<Road> getRoads(){
		return model.getRoads();
	}
	
	public Road getRoad(int road){
		return model.getRoad(road);
	}
	
	public int getStep() {
		return model.getStep();
	}
	
	public void nextStep() {
		model.nextStep();
	}
	
	public boolean reset() {
		return init();
	}
	
	private boolean init(){
		model = new Run();
		return true;
	}
}
