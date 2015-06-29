package edu.hm.am.stausimulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import edu.hm.am.stausimulator.data.RoundData;

/**
 * Basic Model Class
 * 
 * @author Luca Spataro
 */
public abstract class Model extends Observable {
	
	// random number generator
	protected static final Random	RANDOM	= new Random();
	
	private Observer				roadObserver;
	
	// road
	private Road					road;
	
	// lanes of road
	private List<Lane>				lanes;
	
	// data per round
	private List<RoundData>			data;
	
	private boolean					inited	= false;
	
	public Model() {
	}
	
	public abstract void stage0();
	
	public abstract void stage1();
	
	public abstract void stage2();
	
	public abstract void stage3();
	
	public abstract void stage4();
	
	/**
	 * execute next Step by executing all stages
	 */
	public void nextStep() {
		if (!inited) {
			init();
		}
		stage0();
		stage1();
		stage2();
		stage3();
		stage4();
		
		notifyObservers("Step");
	}
	
	public Road getRoad() {
		return road;
	}
	
	public void setRoad(Road road) {
		this.road = road;
		init();
	}
	
	public List<Lane> getLanes() {
		return lanes;
	}
	
	public void setLanes(List<Lane> lanes) {
		this.lanes = lanes;
	}
	
	public Lane getLane(int index) {
		return lanes.get(index);
	}
	
	public List<RoundData> getData() {
		return data;
	}
	
	public int getLaneCount() {
		return road.getNumberOfLanes();
	}
	
	public int getCellCount() {
		return road.getNumberOfCells();
	}
	
	/**
	 * Calculate distance to the next car on lane.
	 * 
	 * @param lane Lane to search in
	 * @param index Position from where to search forward
	 * @return Distance to next Car
	 */
	protected int getForwardDistance(Lane lane, int index) {
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
	
	/**
	 * Check whether or not a Car can change to the Lane. Checks forward and
	 * backward distance on road from index.
	 * 
	 * @param lane Lane to check
	 * @param index Position of Car
	 * @return Wheter the Car can change
	 */
	protected boolean canChangeTo(Lane lane, int index) {
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
			
			// if distance is already greater than the max velocity,
			// we don't care anymore and stop
			if (distance > lane.getMaxVelocity()) {
				break;
			}
			
			// at start of road switch to prev lane
			if (i == 0) {
				l = l.getPrev();
				
				if (l == null) {
					break;
				}
				
				cellCount = l.getNumberOfCells();
				i = cellCount;
			}
		}
		
		// distance >= MAX_VELOCITY or vehicle.getSpeed() - 2 <= distance
		return distance >= lane.getMaxVelocity() || (vehicle != null && ((vehicle.getSpeed() - 2) <= distance));
	}
	
	/**
	 * Initialize Model
	 */
	protected void init() {
		data = new ArrayList<RoundData>();
		lanes = new ArrayList<Lane>();
		
		for (int i = 0; i < getLaneCount(); i++) {
			lanes.add(new Lane(road));
		}
		
		roadObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if ("Changed Lanes".equals(arg)) {
					for (int i = 0; i < lanes.size(); i++) {
						lanes.get(i).destroy();
					}
					data = new ArrayList<RoundData>();
					lanes = new ArrayList<Lane>();
					for (int i = 0; i < getLaneCount(); i++) {
						lanes.add(new Lane(road));
					}
				}
			}
		};
		road.addObserver(roadObserver);
		
		inited = true;
	}
	
	@Override
	public void notifyObservers(Object arg) {
		setChanged();
		super.notifyObservers(arg);
	}
	
	/**
	 * Destroy Model
	 */
	public void destroy() {
		deleteObservers();
		road.deleteObserver(roadObserver);
		
		for (Lane lane : lanes) {
			lane.destroy();
		}
		
		road = null;
		data = null;
		lanes = null;
		roadObserver = null;
		
		try {
			finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
