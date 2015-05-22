/*
 * 
 */
package edu.hm.am.stausimulator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import edu.hm.am.stausimulator.Configuration;
import edu.hm.am.stausimulator.Property;
import edu.hm.am.stausimulator.RunData;
import edu.hm.am.stausimulator.VehiclePool;

// TODO: Auto-generated Javadoc
/**
 * The Class Lane.
 */
public class Lane {

	/**
	 * The Enum Direction.
	 */
	public enum Direction {
		/** The north. */
		NORTH,
		/** The south. */
		SOUTH
	}

	private static final Random random = new Random();

	/** The direction. */
	private final Direction direction;

	/** The cells. */
	private List<Cell> cells;

	private RunData data;

	/**
	 * Instantiates a new lane.
	 */
	public Lane() {
		this(Direction.NORTH);
	}

	/**
	 * Instantiates a new lane.
	 *
	 * @param direction the direction
	 */
	public Lane(Direction direction) {
		this(direction, Configuration.getProperty(Property.CELLS).intValue());
	}

	/**
	 * Instantiates a new lane.
	 *
	 * @param direction the direction
	 * @param cells the cells
	 */
	public Lane(Direction direction, int cells) {
		this.direction = direction;
		this.cells = new ArrayList<Cell>(cells);
		for (int i = 0; i < cells; i++) {
			this.cells.add(new Cell());
		}

		this.data = new RunData();
		
		double density = Configuration.getProperty(Property.DENSITY).doubleValue();

		List<Vehicle> cars = VehiclePool.get((new Double(density * cells)).intValue());

		Cell cell;
		for (Vehicle car : cars) {
			while (true) {
				cell = this.cells.get(random.nextInt(cells));
				if (cell.isFree()) {
					cell.setVehicle(car);
					break;
				}
			}
		}
	}

	/**
	 * Gets the direction.
	 *
	 * @return the direction
	 */
	public Direction getDirection() {
		return direction;
	}

	public List<Cell> getCells() {
		return cells;
	}

	public List<Integer> export() {
		List<Integer> data = new ArrayList<>();
		Integer value = null;

		Iterator<Cell> it = cells.listIterator();
		Cell cell = null;

		while (it.hasNext()) {
			cell = it.next();
			if (cell.isFree()) {
				value = null;
			}
			else {
				value = cell.getVehicle().getSpeed();
			}
			data.add(value);
		}
		return data;
	}

	public void update() {
		data.push(export());
		
		// update logic
		Random random = new Random();
		double probability = Configuration.getProperty(Property.PROBABILITY).doubleValue();
		int maxspeed = Configuration.getProperty(Property.MAX_SPEED).intValue();
		int distance = 0;

		Cell cell;
		Vehicle vehicle;
		for (int i = 0; i < cells.size(); i++) {
			cell = cells.get(i);

			if (!cell.isFree()) {
				vehicle = cell.getVehicle();

				distance = getDistance(i, cells);

				// step 1 - speed up
				if (vehicle.getSpeed() < maxspeed && distance > vehicle.getSpeed() + 1) {
					vehicle.speedUp();
				}

				// step 2 - break
				if (vehicle.getSpeed() > distance) {
					vehicle.setSpeed(distance);
				}

				// step 3 - randomization
				if (random.nextDouble() < probability) {
					vehicle.slowDown();
				}

			}
		}

		// step 4 - move cars
		int newpos;
		List<Integer> flags = new ArrayList<Integer>();

		for (int i = 0; i < cells.size(); i++) {
			cell = cells.get(i);
			if (!cell.isFree() && !flags.contains(i)) {
				vehicle = cell.getVehicle();
				newpos = i + cell.getVehicle().getSpeed();
				newpos = newpos >= cells.size() ? newpos - cells.size() : newpos;

				cell.setVehicle(null);
				cells.get(newpos).setVehicle(vehicle);
				flags.add(newpos);
			}
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
		StringBuffer sb = new StringBuffer();

		for (Cell cell : cells) {
			if (cell.isFree()) {
				sb.append("-");
			}
			else {
				sb.append(cell.getVehicle().getSpeed());
			}
		}

		return sb.toString();
	}
}
