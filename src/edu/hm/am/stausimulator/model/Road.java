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
import edu.hm.am.stausimulator.data.RoundData;

// TODO: Auto-generated Javadoc
/**
 * The Class Road.
 */
public class Road extends Observable {

	public static final Random RANDOM = new Random();

	private int numberOfLanes;

	private int step;

	private int numberOfCells;

	private int maxVelocity;

	private double density;

	private boolean cruiseControl; // TODO: implement in logic

	private boolean allowLaneChange;

	private List<Lane> lanes;

	/**
	 * Instantiates a new road.
	 *
	 * @param lanes the lanes
	 */
	public Road() {
		step = 0;
		numberOfLanes = Defaults.NUMBER_OF_LANES;
		numberOfCells = Defaults.NUMBER_OF_CELLS;
		maxVelocity = Defaults.MAX_VELOCITY;
		density = Defaults.DENSITY;

		init();
	}

	public int getStep() {
		return step;
	}

	public int getNumberOfCells() {
		return numberOfCells;
	}

	public int getNumberOfLanes() {
		return numberOfLanes;
	}

	public List<Lane> getLanes() {
		return lanes;
	}

	public void setLanes(int lanes) {
		numberOfLanes = lanes;
		init();
		setChanged();
		notifyObservers("Changed Lanes");
	}

	public void setCells(int cells) {
		numberOfCells = cells;
		setChanged();
		notifyObservers("Changed Cells");
	}

	public int getMaxVelocity() {
		return maxVelocity;
	}

	public void setMaxVelocity(int maxVelocity) {
		this.maxVelocity = maxVelocity;
		setChanged();
		notifyObservers("Changed Max Velocity");
	}

	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		this.density = density;
		setChanged();
		notifyObservers("Changed Density");
	}

	public boolean isAllowLaneChange() {
		return allowLaneChange;
	}

	public void setAllowLaneChange(boolean allowChange) {
		allowLaneChange = allowChange;
		if (allowLaneChange) {
			// link each road to itself
			for (Lane lane : lanes) {
				lane.setPrev(lane);
				lane.setNext(lane);
			}
		}
		setChanged();
		notifyObservers("Changed Allow Lane Change");
	}

	public boolean isCruiseControl() {
		return cruiseControl;
	}

	public void setCruiseControl(boolean cruiseControl) {
		this.cruiseControl = cruiseControl;
		// TODO: implement logic
	}

	public int nextStep() {

		List<Cell> cells;
		List<RoundData> data = new ArrayList<>();

		Lane lane;
		Cell cell;
		Vehicle vehicle;

		int laneCount = lanes.size();
		int cellCount = lanes.get(0).getNumberOfCells();
		int distance;

		// clear
		for (int l = 0; l < laneCount; l++) {
			data.add(new RoundData());
			lanes.get(l).clear();
		}

		// stage zero
		setChanged();
		notifyObservers("Stage 0 - Done");

		// stage one - speed up
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
		setChanged();
		notifyObservers("Stage 1 - Done");

		if (allowLaneChange) {
			int distanceLeftLane;
			int distanceRightLane;

			Lane leftLane;
			Lane rightLane;

			for (int l = 0; l < laneCount; l++) {
				lane = lanes.get(l);
				cells = lane.getCells();

				for (int c = 0; c < cellCount; c++) {
					cell = cells.get(c);

					vehicle = cell.getVehicle();
					if (vehicle != null) {
						distance = getForwardDistance(lane, c);

						if (vehicle.getSpeed() > distance) {
							distanceLeftLane = l > 0 ? getForwardDistance(lanes.get(l - 1), c) : -1;
							if (distanceLeftLane > 0 && distanceLeftLane > distance) {
								leftLane = lanes.get(l - 1);
								if (leftLane.getCells().get(c).isFree() && canChangeTo(leftLane, c)) {
									vehicle.setChangedLane();
									cell.setVehicle(null);
									leftLane.getCells().get(c).setVehicle(vehicle);
									continue;
								}
							}

							distanceRightLane = (l + 1) < lanes.size() ? getForwardDistance(lanes.get(l + 1), c) : -1;
							if (distanceRightLane > 0 && distanceRightLane > distance) {
								rightLane = lanes.get(l + 1);
								if (rightLane.getCells().get(c).isFree() && canChangeTo(rightLane, c)) {
									vehicle.setChangedLane();
									cell.setVehicle(null);
									rightLane.getCells().get(c).setVehicle(vehicle);
									continue;
								}
							}
						}
					}
				}
			}
		}
		setChanged();
		notifyObservers("Stage 1.5 - Lane Change");

		// stage 2 - brake
		for (int l = 0; l < laneCount; l++) {
			lane = lanes.get(l);
			cells = lane.getCells();

			for (int c = 0; c < cellCount; c++) {
				cell = cells.get(c);

				vehicle = cell.getVehicle();
				if (vehicle != null) {
					distance = getForwardDistance(lane, c);
					if (vehicle.getSpeed() > distance) {
						vehicle.setSpeed(distance);
					}
				}
			}
		}
		setChanged();
		notifyObservers("Stage 2 - Done");

		// stage 3 - randomization (linger)
		for (int l = 0; l < laneCount; l++) {
			lane = lanes.get(l);
			cells = lane.getCells();

			for (int c = 0; c < cellCount; c++) {
				cell = cells.get(c);

				vehicle = cell.getVehicle();
				if (vehicle != null) {
					distance = getForwardDistance(lane, c);
					if (RANDOM.nextDouble() < (vehicle.getSpeed() == 1 ? lane.getStartingProbability() : lane.getLingerProbability())) {
						vehicle.linger();
					}
				}
			}
		}
		setChanged();
		notifyObservers("Stage 3 - Done");

		// stage 4 - move cars
		List<String> flags = new ArrayList<String>();
		RoundData stepData;

		int newpos;
		int averageFlow;

		for (int l = 0; l < laneCount; l++) {
			lane = lanes.get(l);
			cells = lane.getCells();

			averageFlow = 0;

			for (int c = 0; c < cellCount; c++) {
				cell = cells.get(c);

				if (!cell.isFree() && cell.getVehicle().getSpeed() > 0 && !flags.contains(l + "|" + c)) {
					vehicle = cell.getVehicle();
					newpos = c + vehicle.getSpeed();

					cell.setVehicle(null);
					if (newpos < cellCount) {
						cells.get(newpos).setVehicle(vehicle);
						flags.add(l + "|" + newpos);
					} else {
						if (lane.getNext() != null) {
							newpos = newpos - cells.size();
							lane.getNext().getCells().get(newpos).setVehicle(vehicle);
							flags.add(lanes.indexOf(lane.getNext()) + "|" + newpos);
						}

					}
					averageFlow++;
				}
			}

			stepData = data.get(l);
			stepData.setVelocityPerCell(lane.export());
			stepData.setAverageFlow(averageFlow);

			lane.addData(stepData);
		}
		setChanged();
		notifyObservers("Stage 4 - Done");

		step++;
		setChanged();
		notifyObservers("Step");

		return step;
	}

	public void save(File directory) throws IOException {
		Lane lane;
		File dir;
		for (int i = 0; i < lanes.size(); i++) {
			lane = lanes.get(i);
			dir = new File(directory, "Lane " + (i + 1));
			dir.mkdir();
			lane.save(dir);
		}
	}

	public void reset() {
		step = 0;
		numberOfLanes = Defaults.NUMBER_OF_LANES;
		numberOfCells = Defaults.NUMBER_OF_CELLS;
		maxVelocity = Defaults.MAX_VELOCITY;
		density = Defaults.DENSITY;

		init();
		setChanged();
		notifyObservers("Reset");
	}

	private void init() {
		lanes = new ArrayList<Lane>();
		for (int i = 0; i < numberOfLanes; i++) {
			lanes.add(new Lane(this));
		}
	}

	private int getForwardDistance(Lane lane, int index) {
		Lane l = lane;

		int distance = 0;
		int cellCount = l.getNumberOfCells();

		Vehicle vehicle = null;
		while (vehicle == null) {
			index++;
			if (index == cellCount) {
				index = 0;
				l = lane.getNext();
				if (l == null) {
					distance = cellCount - index;
					break;
				}
				cellCount = l.getNumberOfCells();
			}

			vehicle = l.getCell(index).getVehicle();

			if (vehicle != null) {
				break;
			}
			distance++;
		}

		return distance;
	}

	private boolean canChangeTo(Lane lane, int index) {
		Lane l = lane;
		Vehicle vehicle = null;

		int distance = 0;
		int cellCount = l.getNumberOfCells();

		// start at prev cell
		for (int i = index - 1; i >= 0; i--) {
			if (!l.getCells().get(i).isFree()) {
				vehicle = l.getCells().get(i).getVehicle();
				break;
			}
			distance++;

			if (distance > getMaxVelocity()) {
				break;
			}

			if (i == 0) {
				l = l.getPrev();

				if (l == null) {
					break;
				}

				cellCount = l.getNumberOfCells();
				i = cellCount;
			}
		}

		return distance >= getMaxVelocity() || (vehicle != null && vehicle.getSpeed() - 2 <= distance);
	}

}
