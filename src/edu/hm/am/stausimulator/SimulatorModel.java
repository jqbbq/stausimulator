package edu.hm.am.stausimulator;

import edu.hm.am.stausimulator.model.Lane;
import edu.hm.am.stausimulator.model.Road;

public class SimulatorModel {

	private Road road;

	public SimulatorModel() {
		road = new Road(1);

		for (Lane lane : road.getLanes()) {
			System.out.println(lane.toString());
		}
	}

	public Road getRoad() {
		return road;
	}

	public void nextStep() {
		// update lanes
		for (Lane lane : road.getLanes()) {
			lane.update();
		}
	}

}
