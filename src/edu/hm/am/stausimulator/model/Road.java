/*
 * 
 */
package edu.hm.am.stausimulator.model;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import edu.hm.am.stausimulator.Defaults;

// TODO: Auto-generated Javadoc
/**
 * The Class Road.
 */
public class Road extends Observable {
	
	public static final Random	RANDOM	= new Random();
	
	private int					numberOfLanes;
	
	private int					step;
	
	private int					numberOfCells;
	
	private int					maxVelocity;
	
	private double				density;
	
	private boolean				cruiseControl;			// TODO: implement in
														// logic
														
	private Model				model;
	
	private Observer			modelObserver;
	
	/**
	 * Instantiates a new road.
	 *
	 * @param lanes the lanes
	 */
	public Road() {
		this(Defaults.NUMBER_OF_LANES, Defaults.NUMBER_OF_CELLS, Defaults.MAX_VELOCITY, Defaults.DENSITY);
	}
	
	public Road(int numberOfLanes, int numberOfCells, int maxVelocity, double density) {
		
		this.numberOfLanes = numberOfLanes;
		this.numberOfCells = numberOfCells;
		this.maxVelocity = maxVelocity;
		this.density = density;
		
		model = new StandardModel();
		model.setRoad(this);
		
		// tunnel model events through
		modelObserver = new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				setChanged();
				notifyObservers(arg);
			}
		};
		
		model.addObserver(modelObserver);
		
		step = 0;
		
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
		return model.getLanes();
	}
	
	public Lane getLane(int index) {
		if (index < 0 || index > getNumberOfLanes()) {
			return null;
		}
		return model.getLane(index);
	}
	
	public void setLanes(int lanes) {
		numberOfLanes = lanes;
		model.notifyObservers("Changed Lanes");
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
	
	public boolean isCruiseControl() {
		return cruiseControl;
	}
	
	public void setCruiseControl(boolean cruiseControl) {
		this.cruiseControl = cruiseControl;
		// TODO: implement logic
	}
	
	public Model getModel() {
		return model;
	}
	
	public void setModel(Model model) {
		// destory old model
		this.model.destroy();
		
		this.model = model;
		this.model.addObserver(modelObserver);
		
		setChanged();
		notifyObservers("Changed Model");
	}
	
	public void nextStep() {
		model.nextStep();
		step++;
	}
	
	public void save(File directory) throws IOException {
		List<Lane> lanes = model.getLanes();
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
		
		model = new StandardModel();
		model.setRoad(this);
		
		model.addObserver(modelObserver);
		
		setChanged();
		notifyObservers("Reset");
	}
	
	public void destory() {
		deleteObservers();
		model.deleteObserver(modelObserver);
		
		model.destroy();
		model = null;
		
		try {
			finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}