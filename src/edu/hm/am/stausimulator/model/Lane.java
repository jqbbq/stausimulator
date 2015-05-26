/*
 * 
 */
package edu.hm.am.stausimulator.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.hm.am.stausimulator.chart.VDRChart;
import edu.hm.am.stausimulator.data.LaneData;
import edu.hm.am.stausimulator.factory.VehicleFactory;

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

	/** The direction. */
	private final Direction direction;

	private final Road road;

	// private final int id;

	/** The cells. */
	private List<Cell> cells;

	private LaneData data;

	/**
	 * Instantiates a new lane.
	 */
	public Lane(Road road) {
		this(road, Direction.NORTH);
	}

	/**
	 * Instantiates a new lane.
	 *
	 * @param direction the direction
	 */
	public Lane(Road road, Direction direction) {
		this.road = road;
		this.direction = direction;
		data = new LaneData(this);
		cells = new ArrayList<Cell>(road.getCells());
		for (int i = 0; i < road.getCells(); i++) {
			cells.add(new Cell());
		}

		List<Vehicle> cars = VehicleFactory.createVehicles((int) (road.getDensity() * road.getCells()), road.getMaxSpeed());

		Cell cell;
		for (Vehicle car : cars) {
			while (true) {
				cell = cells.get(Road.RANDOM.nextInt(cells.size()));
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

	public LaneData getData() {
		return data;
	}

	public int getMaxSpeed() {
		return road.getMaxSpeed();
	}

	public int getCellsCount() {
		return cells.size();
	}

	public void save(File directory) throws IOException {
		// write CSV
		File csv = new File(directory, "lane.csv");
		FileWriter writer = new FileWriter(csv);

		writer.append("Steps;" + road.getStep() + "\n");
		writer.append("Cells;" + road.getCells() + "\n");

		data.write(writer);

		writer.flush();
		writer.close();

		// write VDR-Diagramm
		VDRChart.write(data, new File(directory, "lane.png"));
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
			} else {
				value = cell.getVehicle().getSpeed();
				if (cell.getVehicle().hasLingered()) {
					value *= -1;
				}
			}
			data.add(value);
		}

		return data;
	}

	public void clear() {
		data.add(export());
		for (Cell cell : cells) {
			if (!cell.isFree()) {
				cell.getVehicle().setLingered(false);
			}
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		for (Cell cell : cells) {
			if (cell.isFree()) {
				sb.append("-");
			} else {
				sb.append(cell.getVehicle().getSpeed());
			}
		}

		return sb.toString();
	}
}
