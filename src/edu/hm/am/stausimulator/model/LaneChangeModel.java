package edu.hm.am.stausimulator.model;

import java.util.List;

public class LaneChangeModel extends StandardModel {
	
	public LaneChangeModel() {
		super();
	}
	
	public LaneChangeModel(Road road) {
		super(road);
	}
	
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
}
