/*
 * 
 */
package edu.hm.am.stausimulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

// TODO: Auto-generated Javadoc
/**
 * The Class Road.
 */
public class Road extends Observable {

	private final int id;

	/** The lanes. */
	private List<Lane> lanes;

	private int laneCount;

	private int step;

	private int cells;

	private int maxSpeed;

	private double density;

	private double probability;

	private double startingProbability;

	/**
	 * Instantiates a new road.
	 *
	 * @param lanes the lanes
	 */
	public Road(int id) {
		this.id = id;

		step = 0;

		laneCount = 1;
		cells = 20;
		maxSpeed = 5;
		density = 0.2;
		probability = 0.2;
		startingProbability = 0.2;

		init();
	}

	public int getId() {
		return id;
	}

	public List<Lane> getLanes() {
		return lanes;
	}

	public void setLanes(int lanes) {
		laneCount = lanes;
		init();
		setChanged();
		notifyObservers();
	}

	public int getStep() {
		return step;
	}

	public int getCells() {
		return cells;
	}

	public void setCells(int cells) {
		this.cells = cells;
		init();
		setChanged();
		notifyObservers();
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		this.density = density;
		init();
		setChanged();
		notifyObservers();
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public double getStartingProbability() {
		return startingProbability;
	}

	public void setStartingProbability(double startingProbability) {
		this.startingProbability = startingProbability;
	}

	public int nextStep() {
		for (Lane lane : lanes) {
			lane.update();
		}
		step++;
		return step;
	}

	private void init() {
		lanes = new ArrayList<Lane>();
		for (int i = 0; i < laneCount; i++) {
			lanes.add(new Lane(this));
		}
	}

	@Override
	public String toString() {
		// TODO implement usefull
		return "Road with " + lanes.size() + " lanes.";
	}
}
