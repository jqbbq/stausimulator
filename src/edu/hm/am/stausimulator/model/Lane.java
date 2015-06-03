/*
 * 
 */
package edu.hm.am.stausimulator.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import edu.hm.am.stausimulator.Defaults;
import edu.hm.am.stausimulator.chart.VDRChart;
import edu.hm.am.stausimulator.chart.VDRChart2;
import edu.hm.am.stausimulator.data.LaneData;
import edu.hm.am.stausimulator.data.RoundData;
import edu.hm.am.stausimulator.factory.VehicleFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class Lane.
 */
public class Lane extends Observable {

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

	private double lingerProbability;

	private double startingProbability;

	/** The cells. */
	private List<Cell> cells;

	private Road road;

	private Lane next;
	private Lane prev;

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

		next = this;
		prev = this;
		data = new LaneData(this);

		lingerProbability = Defaults.LINGER_PROBABILITY;
		startingProbability = Defaults.STARTING_PROBABILITY;

		initCells();
		initVehicles();

		data.add(new RoundData(export()));

		road.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if ("Changed Density".equals(arg) || "Changed Max Speed".equals(arg)) {
					initVehicles();
				} else if ("Changed Cells".equals(arg)) {
					initCells();
					initVehicles();
				}
			}
		});
	}

	public Road getRoad() {
		return road;
	}

	public void setRoad(Road road) {
		this.road = road;
	}

	/**
	 * Gets the direction.
	 *
	 * @return the direction
	 */
	public Direction getDirection() {
		return direction;
	}

	public Lane getPrev() {
		return prev;
	}

	public void setPrev(Lane prev) {
		this.prev = prev;

		setChanged();
		notifyObservers("Changed previous lane");
	}

	public Lane getNext() {
		return next;
	}

	public void setNext(Lane next) {
		this.next = next;

		setChanged();
		notifyObservers("Changed previous lane");
	}

	public Cell getCell(int index) {
		return cells.get(index);
	}

	public List<Cell> getCells() {
		return cells;
	}

	public LaneData getData() {
		return data;
	}

	public int getMaxVelocity() {
		return road.getMaxVelocity();
	}

	public int getNumberOfCells() {
		return cells.size();
	}

	public double getLingerProbability() {
		return lingerProbability;
	}

	public void setLingerProbability(double probability) {
		Map<String, Object> changeData = new HashMap<>();
		changeData.put("from", new Double(lingerProbability));
		changeData.put("to", new Double(probability));
		data.addChange(new Integer(road.getStep()), "probability", changeData);

		lingerProbability = probability;

		notifyObservers("Changed Probability");
	}

	public double getStartingProbability() {
		return startingProbability;
	}

	public void setStartingProbability(double probability) {
		Map<String, Object> changeData = new HashMap<>();
		changeData.put("from", new Double(startingProbability));
		changeData.put("to", new Double(probability));
		data.addChange(new Integer(road.getStep()), "startingProbability", changeData);

		startingProbability = probability;

		notifyObservers("Changed Starting Probability");
	}

	public void save(File directory) throws IOException {
		// write CSV
		File csv = new File(directory, "data.csv");
		FileWriter writer = new FileWriter(csv);

		data.write(writer);

		writer.flush();
		writer.close();

		// write VDR-Diagramm
		VDRChart.write(data, new File(directory, "vdr-chart-color.png"));
		VDRChart2.write(data, new File(directory, "vdr-chart-black&white.png"));
	}

	public List<Integer> export() {
		List<Integer> data = new ArrayList<>();
		Integer value = null;

		Iterator<Cell> it = cells.listIterator();
		Cell cell = null;

		while (it.hasNext()) {
			cell = it.next();
			value = null;
			if (!cell.isFree()) {
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
		for (Cell cell : cells) {
			if (!cell.isFree()) {
				cell.getVehicle().setLingered(false);
				cell.getVehicle().setChangedLane(false);
			}
		}
	}

	public void addData(RoundData data) {
		this.data.add(data);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	private void initCells() {
		cells = new ArrayList<Cell>();
		for (int i = 0; i < road.getNumberOfCells(); i++) {
			cells.add(new Cell());
		}
	}

	private void initVehicles() {
		// remove all cars
		for (int i = 0; i < cells.size(); i++) {
			cells.get(i).setVehicle(null);
		}

		// init vehicles
		Cell cell;
		List<Vehicle> vehicles = VehicleFactory.createCars((int) (road.getDensity() * road.getNumberOfCells()), road.getMaxVelocity());
		for (Vehicle vehicle : vehicles) {
			while (true) {
				cell = cells.get(Road.RANDOM.nextInt(cells.size()));
				if (cell.isFree()) {
					cell.setVehicle(vehicle);
					break;
				}
			}
		}
	}
}
