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
import edu.hm.am.stausimulator.chart.AVGFlowChart;
import edu.hm.am.stausimulator.chart.AVGSpeedChart;
import edu.hm.am.stausimulator.chart.PositionChart;
import edu.hm.am.stausimulator.chart.SpeedChart;
import edu.hm.am.stausimulator.data.LaneData;
import edu.hm.am.stausimulator.data.RoundData;
import edu.hm.am.stausimulator.factory.CellFactory;
import edu.hm.am.stausimulator.factory.CellFactory.Distribution;
import edu.hm.am.stausimulator.factory.VehicleFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class Lane.
 */
public class Lane extends Observable {
	
	private double		lingerProbability;
	
	private double		startingProbability;
	
	/** The cells. */
	private List<Cell>	cells;
	
	private Road		road;
	
	private Lane		next;
	private Lane		prev;
	
	private LaneData	data;
	
	private Observer	roadObserver;
	
	/**
	 * Instantiates a new lane.
	 */
	public Lane(Road road) {
		this.road = road;
		
		next = this;
		prev = this;
		data = new LaneData(this);
		
		lingerProbability = Defaults.LINGER_PROBABILITY;
		startingProbability = Defaults.STARTING_PROBABILITY;
		
		init();
		
		roadObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if ("Changed Density".equals(arg) || "Changed Max Velocity".equals(arg) || "Changed Cells".equals(arg)) {
					init();
				}
				else if ("Changed Model".equals(arg)) {
					if (road.getModel() instanceof LaneChangeModel) {
						resetLanes();
					}
				}
			}
		};
		road.addObserver(roadObserver);
	}
	
	public Road getRoad() {
		return road;
	}
	
	public void setRoad(Road road) {
		this.road = road;
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
		notifyObservers("Changed next lane");
	}
	
	public Cell getCell(int index) {
		return cells.get(index);
	}
	
	public List<Cell> getCells() {
		return cells;
	}
	
	public void setCells(List<Cell> cells) {
		this.cells = cells;
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
		
		setChanged();
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
		
		setChanged();
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
		SpeedChart.write(data, new File(directory, "vdr-chart.png"));
		PositionChart.write(data, new File(directory, "position-chart.png"));
		AVGFlowChart.write(data, new File(directory, "average-flow.png"));
		AVGSpeedChart.write(data, new File(directory, "average-speed.png"));
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
	
	private void init() {
		cells = CellFactory.createCells(road.getNumberOfCells(), VehicleFactory.createCars((int) (road.getDensity() * road.getNumberOfCells()), road.getMaxVelocity()), Distribution.EVEN);
	}
	
	private void resetLanes() {
		prev = this;
		next = this;
		setChanged();
		notifyObservers("Changed Prev/Next Lane");
	}
	
	public void destroy() {
		deleteObservers();
		road.deleteObserver(roadObserver);
		
		cells = null;
		data = null;
		prev = null;
		next = null;
		road = null;
		
		roadObserver = null;
		
		try {
			finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
