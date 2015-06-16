package edu.hm.am.stausimulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import edu.hm.am.stausimulator.data.RoundData;

public abstract class Model extends Observable {
	
	protected static final Random	RANDOM	= new Random();
	
	private Observer				roadObserver;
	
	private Road					road;
	
	private List<Lane>				lanes;
	private List<RoundData>			data;
	
	private boolean					inited	= false;
	
	public Model() {
	}
	
	protected Model(Road road) {
		this.road = road;
	}
	
	public abstract void stage0();
	
	public abstract void stage1();
	
	public abstract void stage2();
	
	public abstract void stage3();
	
	public abstract void stage4();
	
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
			
			if (distance > lane.getMaxVelocity()) {
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
		
		return distance >= lane.getMaxVelocity() || (vehicle != null && vehicle.getSpeed() - 2 <= distance);
	}
	
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
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		road.deleteObserver(roadObserver);
	}
}
