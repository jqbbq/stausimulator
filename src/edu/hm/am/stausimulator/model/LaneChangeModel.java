package edu.hm.am.stausimulator.model;

import java.util.List;

/**
 * Implementation of the lane change model.
 * 
 * @author Luca
 */
public class LaneChangeModel extends StandardModel {
	
	public LaneChangeModel() {
		super();
	}
	
	/**
	 * Override stage1 of StandardModel.
	 */
	/*
	 * @see edu.hm.am.stausimulator.model.StandardModel#stage1()
	 */
	@Override
	public void stage1() {
		super.stage1();
		
		List<Lane> lanes = getLanes();
		int laneCount = getLaneCount();
		int cellCount = getCellCount();
		
		Lane lane;
		Lane leftLane;
		Lane rightLane;
		
		Cell cell;
		Vehicle vehicle;
		List<Cell> cells;
		
		int distance;
		int distanceLeftLane;
		int distanceRightLane;
		
		for (int l = 0; l < laneCount; l++) {
			lane = lanes.get(l);
			cells = lane.getCells();
			
			for (int c = cellCount - 1; c >= 0; c--) {
				cell = cells.get(c);
				
				vehicle = cell.getVehicle();
				if (vehicle != null) {
					// get distance to next car on lane
					distance = getForwardDistance(lane, c);
					
					// if vehicle hasn't already changed lane and speed >
					// distance
					if (!vehicle.hasChangedLane() && vehicle.getSpeed() > distance) {
						
						// get distance to next car on left lane
						distanceLeftLane = l > 0 ? getForwardDistance(lanes.get(l - 1), c) : -1;
						if (distanceLeftLane > 0 && distanceLeftLane > distance) {
							leftLane = lanes.get(l - 1);
							
							// check if car can change to left lane
							if (leftLane.getCells().get(c).isFree() && canChangeTo(leftLane, c)) {
								vehicle.setChangedLane();
								cell.setVehicle(null);
								leftLane.getCells().get(c).setVehicle(vehicle);
								continue;
							}
						}
						
						// get distance to next car on right lane
						distanceRightLane = (l + 1) < lanes.size() ? getForwardDistance(lanes.get(l + 1), c) : -1;
						if (distanceRightLane > 0 && distanceRightLane > distance) {
							rightLane = lanes.get(l + 1);
							
							// check if car can change to right lane
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
}
