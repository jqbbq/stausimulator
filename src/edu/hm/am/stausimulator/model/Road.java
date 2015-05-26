/*
 * 
 */
package edu.hm.am.stausimulator.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import edu.hm.am.stausimulator.Defaults;

// TODO: Auto-generated Javadoc
/**
 * The Class Road.
 */
public class Road extends Observable {

	public static final Random RANDOM = new Random();

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

		laneCount = Defaults.LANES;
		cells = Defaults.CELLS;
		maxSpeed = Defaults.MAXSPEED;
		density = Defaults.DENSITY;
		probability = Defaults.PROBABILITY;
		startingProbability = Defaults.STARTING_PROBABILITY;

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
		notifyObservers("Changed Lanes");
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
		notifyObservers("Changed Cells");
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
		notifyObservers("Changed Max Speed");
	}

	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		this.density = density;
		init();
		setChanged();
		notifyObservers("Changed Density");
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
		notifyObservers("Changed Probability");
	}

	public double getStartingProbability() {
		return startingProbability;
	}

	public void setStartingProbability(double startingProbability) {
		this.startingProbability = startingProbability;
		notifyObservers("Changed Starting Probability");
	}

	public int nextStep() {

		List<Cell> cells;

		Lane lane;
		Cell cell;
		Vehicle vehicle;

		int laneCount = lanes.size();
		int cellCount = lanes.get(0).getCellsCount();
		int distance;

		// clear
		for (int l = 0; l < laneCount; l++) {
			lanes.get(l).clear();
		}

		// step one
		for (int l = 0; l < laneCount; l++) {
			lane = lanes.get(l);
			cells = lane.getCells();

			for (int c = 0; c < cellCount; c++) {
				cell = cells.get(c);

				vehicle = cell.getVehicle();
				if (vehicle != null) {
					vehicle.speedUp();
				}
			}
		}

		// step two
		for (int l = 0; l < laneCount; l++) {
			lane = lanes.get(l);
			cells = lane.getCells();

			for (int c = 0; c < cellCount; c++) {
				cell = cells.get(c);

				vehicle = cell.getVehicle();
				if (vehicle != null) {
					distance = getDistance(c, cells);
					if (vehicle.getSpeed() > distance) {
						vehicle.setSpeed(distance);
					}
				}
			}
		}

		// step 3 - randomization (linger)
		for (int l = 0; l < laneCount; l++) {
			lane = lanes.get(l);
			cells = lane.getCells();

			for (int c = 0; c < cellCount; c++) {
				cell = cells.get(c);

				vehicle = cell.getVehicle();
				if (vehicle != null) {
					distance = getDistance(c, cells);
					if (RANDOM.nextDouble() < (vehicle.getSpeed() == 1 ? getStartingProbability() : getProbability())) {
						vehicle.linger();
					}
				}
			}
		}

		// step 4 - move cars
		int newpos;
		List<Integer> flags = new ArrayList<Integer>();

		for (int l = 0; l < laneCount; l++) {
			lane = lanes.get(l);
			cells = lane.getCells();

			for (int c = 0; c < cellCount; c++) {
				cell = cells.get(c);

				if (!cell.isFree() && !flags.contains(c)) {
					vehicle = cell.getVehicle();
					newpos = c + cell.getVehicle().getSpeed();
					newpos = newpos >= cells.size() ? newpos - cells.size() : newpos;

					cell.setVehicle(null);
					cells.get(newpos).setVehicle(vehicle);
					flags.add(newpos);
				}
			}
		}

		step++;
		notifyObservers("Step");
		return step;
	}

	public void save(File directory) throws IOException {
		File dir = new File(directory, "Road " + id);
		dir.mkdir();

		for (Lane lane : lanes) {
			lane.save(dir);
		}
	}

	private void init() {
		lanes = new ArrayList<Lane>();
		for (int i = 0; i < laneCount; i++) {
			lanes.add(new Lane(this));
		}
	}

	private int getDistance(int index, List<Cell> cells) {
		int distance = 0;

		// start at next cell
		index = (index + 1) % cells.size();
		for (int i = index; i < cells.size(); i++) {
			if (!cells.get(i).isFree()) {
				break;
			}
			distance++;
			if (i == cells.size() - 1) {
				i = -1;
			}
		}

		return distance;
	}

	@Override
	public String toString() {
		// TODO implement usefull
		return "Road with " + lanes.size() + " lanes.";
	}
}
