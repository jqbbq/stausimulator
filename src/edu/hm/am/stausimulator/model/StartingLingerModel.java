package edu.hm.am.stausimulator.model;

import java.util.List;

/**
 * Implementation of the starting linger model.
 * 
 * @author Luca Spataro
 *
 */
public class StartingLingerModel extends StandardModel {
	
	public StartingLingerModel() {
		super();
	}
	
	/**
	 * Override stage3 from StandardModel.
	 */
	/*
	 * @see edu.hm.am.stausimulator.model.StandardModel#stage3()
	 */
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
					// if speed == 1, linger with greater probability
					if (RANDOM.nextDouble() < (vehicle.getSpeed() == 1 ? lane.getStartingProbability() : lane.getLingerProbability())) {
						vehicle.linger();
					}
				}
			}
		}
		setChanged();
		notifyObservers("Stage 3 - Done");
	}
}
