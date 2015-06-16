package edu.hm.am.stausimulator.model;

import java.util.ArrayList;
import java.util.List;

import edu.hm.am.stausimulator.data.RoundData;

public class StandardModel extends Model {
	
	public StandardModel() {
		super();
	}
	
	protected StandardModel(Road road) {
		super(road);
	}
	
	@Override
	public void stage0() {
		List<RoundData> data = getData();
		List<Lane> lanes = getLanes();
		int laneCount = getLaneCount();
		
		data.clear();
		for (int l = 0; l < laneCount; l++) {
			data.add(new RoundData());
			lanes.get(l).clear();
		}
		notifyObservers("Stage 0 - Done");
	}
	
	@Override
	public void stage1() {
		// stage one - speed up
		
		List<Lane> lanes = getLanes();
		int laneCount = getLaneCount();
		int cellCount = getCellCount();
		
		Lane lane;
		Cell cell;
		Vehicle vehicle;
		List<Cell> cells;
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
		
		notifyObservers("Stage 1 - Done");
	}
	
	@Override
	public void stage2() {
		// stage 2 - brake
		
		List<Lane> lanes = getLanes();
		List<Cell> cells;
		
		Lane lane;
		Cell cell;
		Vehicle vehicle;
		
		int laneCount = getLaneCount();
		int cellCount = getCellCount();
		int distance;
		
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
		notifyObservers("Stage 2 - Done");
		
	}
	
	@Override
	public void stage3() {
		// stage 3 - randomization (linger)
		
		List<Lane> lanes = getLanes();
		int laneCount = getLaneCount();
		int cellCount = getCellCount();
		
		Lane lane;
		Cell cell;
		Vehicle vehicle;
		List<Cell> cells;
		
		for (int l = 0; l < laneCount; l++) {
			lane = lanes.get(l);
			cells = lane.getCells();
			
			for (int c = 0; c < cellCount; c++) {
				cell = cells.get(c);
				
				vehicle = cell.getVehicle();
				if (vehicle != null) {
					if (RANDOM.nextDouble() < lane.getLingerProbability()) {
						vehicle.linger();
					}
				}
			}
		}
		notifyObservers("Stage 3 - Done");
	}
	
	@Override
	public void stage4() {
		
		List<RoundData> data = getData();
		List<Lane> lanes = getLanes();
		int laneCount = getLaneCount();
		int cellCount = getCellCount();
		
		Lane lane;
		Cell cell;
		Vehicle vehicle;
		RoundData stepData;
		
		List<Cell> cells;
		List<String> flags = new ArrayList<String>();
		
		int newpos;
		double flow;
		
		for (int l = 0; l < laneCount; l++) {
			lane = lanes.get(l);
			cells = lane.getCells();
			
			flow = 0;
			
			for (int c = 0; c < cellCount; c++) {
				cell = cells.get(c);
				
				if (!cell.isFree()) {
					
					if (cell.getVehicle().getSpeed() > 0 && !flags.contains(l + "|" + c)) {
						vehicle = cell.getVehicle();
						newpos = c + vehicle.getSpeed();
						
						cell.setVehicle(null);
						if (newpos < cellCount) {
							cells.get(newpos).setVehicle(vehicle);
							flags.add(l + "|" + newpos);
						}
						else {
							if (lane.getNext() != null) {
								newpos = newpos - cells.size();
								lane.getNext().getCells().get(newpos).setVehicle(vehicle);
								flags.add(lanes.indexOf(lane.getNext()) + "|" + newpos);
							}
							
						}
						flow++;
					}
				}
			}
			
			stepData = data.get(l);
			stepData.setVelocityPerCell(lane.export());
			stepData.setFlow(flow);
			
			lane.addData(stepData);
		}
		notifyObservers("Stage 4 - Done");
	}
}
