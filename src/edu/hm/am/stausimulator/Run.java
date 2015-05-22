package edu.hm.am.stausimulator;

import java.util.ArrayList;
import java.util.List;

import edu.hm.am.stausimulator.model.Lane;
import edu.hm.am.stausimulator.model.Road;

public class Run {

	private List<Road> roads;

	private int step = 0;

	public Run() {
		this(Configuration.getProperty(Property.ROADS).intValue(), Configuration.getProperty(Property.LANES).intValue());
	}

	public Run(int roads, int lanes) {
		this.roads = new ArrayList<>();

		for (int i = 0; i < roads; i++) {
			this.roads.add(new Road(i + 1, lanes));
		}
	}

	public List<Road> getRoads() {
		return roads;
	}

	public Road getRoad(int road) {
		return roads.get(road);
	}

	public int getStep() {
		return step;
	}

	public void nextStep() {
		for (Road road : roads) {
			for (Lane lane : road.getLanes()) {
				// update lane
				lane.update();
			}
		}
		step++;
	}

}
