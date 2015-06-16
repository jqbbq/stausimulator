package edu.hm.am.stausimulator.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import edu.hm.am.stausimulator.model.Cell;
import edu.hm.am.stausimulator.model.Lane;
import edu.hm.am.stausimulator.model.Lane.Direction;
import edu.hm.am.stausimulator.model.Model;
import edu.hm.am.stausimulator.model.Road;
import edu.hm.am.stausimulator.model.Vehicle;

public class LaneChangeModelTest {
	
	@Test
	public void testMaxVelocity1Density50() {
		
		Road road = new Road(1, 10, 1, .5);
		Model model = road.getModel();
		Lane lane = null;
		Cell cell = null;
		Vehicle vehicle = null;
		
		List<Lane> lanes = road.getLanes();
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		List<Integer> positions = new ArrayList<Integer>();
		List<Cell> cells = null;
		
		Assert.assertEquals(1, lanes.size());
		
		lane = lanes.get(0);
		lane.setLingerProbability(0.0);
		cells = lane.getCells();
		
		Assert.assertEquals(1, lane.getMaxVelocity());
		Assert.assertEquals(Direction.NORTH, lane.getDirection());
		Assert.assertEquals(0.0, lane.getLingerProbability(), 0.0);
		Assert.assertEquals(lane, lane.getPrev());
		Assert.assertEquals(lane, lane.getNext());
		Assert.assertEquals(10, cells.size());
		Assert.assertEquals(road, lane.getRoad());
		
		int numberOfCars = 0;
		for (int i = 0; i < cells.size(); i++) {
			cell = cells.get(i);
			if (!cell.isFree()) {
				numberOfCars++;
				vehicle = cell.getVehicle();
				Assert.assertEquals(0, vehicle.getSpeed());
				vehicles.add(vehicle);
				positions.add(i);
			}
		}
		Assert.assertEquals(5, numberOfCars);
		
		model.stage0();
		
		// stage 1 - everybody speeds up
		model.stage1();
		for (Vehicle v : vehicles) {
			Assert.assertEquals(1, v.getSpeed());
		}
		
		// stage 2 - nobody has to break
		model.stage2();
		for (Vehicle v : vehicles) {
			Assert.assertEquals(1, v.getSpeed());
		}
		
		// stage 3 - skip
		
		// stage 4 - move cars
		model.stage4();
		int listIndex = 0;
		for (int i = 0; i < cells.size(); i++) {
			cell = cells.get(i);
			if (!cell.isFree()) {
				Assert.assertEquals(positions.get(listIndex++) + 1, i);
			}
		}
		
		// do it again to check if car comes in in the front
		model.stage0();
		model.stage1();
		model.stage2();
		model.stage3();
		model.stage4();
		
		listIndex = 0;
		for (int i = 0; i < cells.size(); i++) {
			cell = cells.get(i);
			if (!cell.isFree()) {
				Assert.assertEquals(positions.get(listIndex++).intValue(), i);
			}
		}
	}
	
	@Test
	public void testMaxVelocity5Density50() {
		Road road = new Road(1, 10, 5, .5);
		Model model = road.getModel();
		Lane lane = null;
		Cell cell = null;
		Vehicle vehicle = null;
		
		List<Lane> lanes = road.getLanes();
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		List<Integer> positions = new ArrayList<Integer>();
		List<Cell> cells = null;
		
		Assert.assertEquals(1, lanes.size());
		
		lane = lanes.get(0);
		lane.setLingerProbability(0.0);
		cells = lane.getCells();
		
		Assert.assertEquals(5, lane.getMaxVelocity());
		Assert.assertEquals(Direction.NORTH, lane.getDirection());
		Assert.assertEquals(0.0, lane.getLingerProbability(), 0.0);
		Assert.assertEquals(lane, lane.getPrev());
		Assert.assertEquals(lane, lane.getNext());
		Assert.assertEquals(10, cells.size());
		Assert.assertEquals(road, lane.getRoad());
		
		int numberOfCars = 0;
		for (int i = 0; i < cells.size(); i++) {
			cell = cells.get(i);
			if (!cell.isFree()) {
				numberOfCars++;
				vehicle = cell.getVehicle();
				Assert.assertEquals(0, vehicle.getSpeed());
				vehicles.add(vehicle);
				positions.add(i);
			}
		}
		Assert.assertEquals(5, numberOfCars);
		
		model.stage0();
		
		// stage 1 - everybody speeds up
		model.stage1();
		for (Vehicle v : vehicles) {
			Assert.assertEquals(1, v.getSpeed());
		}
		
		// stage 2 - nobody has to break
		model.stage2();
		for (Vehicle v : vehicles) {
			Assert.assertEquals(1, v.getSpeed());
		}
		
		// stage 3 - skip
		
		// stage 4 - move cars
		model.stage4();
		int listIndex = 0;
		for (int i = 0; i < cells.size(); i++) {
			cell = cells.get(i);
			if (!cell.isFree()) {
				Assert.assertEquals(positions.get(listIndex++) + 1, i);
			}
		}
		
		model.stage0();
		// stage 1 - everybody speeds up
		model.stage1();
		for (Vehicle v : vehicles) {
			Assert.assertEquals(2, v.getSpeed());
		}
		
		// stage 2 - everybody has to break
		model.stage2();
		for (Vehicle v : vehicles) {
			Assert.assertEquals(1, v.getSpeed());
		}
		
		// stage 3 - skip
		
		// stage 4 - move cars
		model.stage4();
		listIndex = 0;
		for (int i = 0; i < cells.size(); i++) {
			cell = cells.get(i);
			if (!cell.isFree()) {
				Assert.assertEquals(positions.get(listIndex++).intValue(), i);
			}
		}
	}
	
}
