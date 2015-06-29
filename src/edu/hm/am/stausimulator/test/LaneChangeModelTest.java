package edu.hm.am.stausimulator.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import edu.hm.am.stausimulator.factory.CellFactory;
import edu.hm.am.stausimulator.factory.CellFactory.Distribution;
import edu.hm.am.stausimulator.factory.VehicleFactory;
import edu.hm.am.stausimulator.model.Cell;
import edu.hm.am.stausimulator.model.Lane;
import edu.hm.am.stausimulator.model.LaneChangeModel;
import edu.hm.am.stausimulator.model.Model;
import edu.hm.am.stausimulator.model.Road;

public class LaneChangeModelTest {
	
	@Test
	public void testMaxVelocity5Density50() {
		
		Road road = new Road(2, 10, 5, .5);
		
		List<Lane> lanes = new ArrayList<>();
		Lane lane = null;
		
		// initiate lane [null, null, null, null, null, 0, 0, 0, 0, 0]
		lane = new Lane(road);
		lane.setLingerProbability(0);
		lane.setCells(CellFactory.createCells(road.getNumberOfCells(), VehicleFactory.createCars((int) (road.getDensity() * road.getNumberOfCells()), road.getMaxVelocity()), Distribution.END));
		lanes.add(lane);
		
		// initiate lane [0, 0, 0, 0, 0, null, null, null, null, null]
		lane = new Lane(road);
		lane.setLingerProbability(0);
		lane.setCells(CellFactory.createCells(road.getNumberOfCells(), VehicleFactory.createCars((int) (road.getDensity() * road.getNumberOfCells()), road.getMaxVelocity()), Distribution.START));
		lanes.add(lane);
		
		Model model = new LaneChangeModel();
		model.setRoad(road);
		model.setLanes(lanes);
		
		int vehicles = 0;
		List<Cell> cells = null;
		Cell cell = null;
		
		// check first lane [null, null, null, null, null, 0, 0, 0, 0, 0]
		lane = lanes.get(0);
		cells = lane.getCells();
		for (int i = cells.size() - 1; i >= 0; i--) {
			cell = cells.get(i);
			if (!cell.isFree()) {
				vehicles++;
			}
			if (i >= 5) {
				Assert.assertFalse(cell.isFree());
			}
			else {
				Assert.assertTrue(cell.isFree());
			}
		}
		Assert.assertEquals(5, vehicles);
		
		// check second lane [0, 0, 0, 0, 0, null, null, null, null, null]
		vehicles = 0;
		lane = lanes.get(1);
		cells = lane.getCells();
		for (int i = 0; i < cells.size(); i++) {
			cell = cells.get(i);
			if (!cell.isFree()) {
				vehicles++;
			}
			if (i < 5) {
				Assert.assertFalse(cell.isFree());
			}
			else {
				Assert.assertTrue(cell.isFree());
			}
		}
		Assert.assertEquals(5, vehicles);
		
		model.stage0();
		model.stage1();
		
		// check first lane, we should have the same amount of cars, but an even
		// distribution [null, 1, null, 1, null, 1, null, 1, null, 1]
		vehicles = 0;
		lane = lanes.get(0);
		cells = lane.getCells();
		for (int i = 0; i < cells.size(); i++) {
			cell = cells.get(i);
			if (!cell.isFree()) {
				vehicles++;
			}
			if (i % 2 == 1) {
				Assert.assertFalse(cell.isFree());
			}
			else {
				Assert.assertTrue(cell.isFree());
			}
		}
		Assert.assertEquals(5, vehicles);
		
		// check second lane, we should have the same amount of cars, but an
		// even distribution [1, null, 1, null, 1, null, 1, null, 1, null]
		vehicles = 0;
		lane = lanes.get(1);
		cells = lane.getCells();
		for (int i = 0; i < cells.size(); i++) {
			cell = cells.get(i);
			if (!cell.isFree()) {
				vehicles++;
			}
			if (i % 2 == 0) {
				Assert.assertFalse(cell.isFree());
			}
			else {
				Assert.assertTrue(cell.isFree());
			}
		}
		Assert.assertEquals(5, vehicles);
		
		model.stage2();
		
		// check first lane, we should have the same amount of cars, but an even
		// distribution [null, 1, null, 1, null, 1, null, 1, null, 1]
		vehicles = 0;
		lane = lanes.get(0);
		cells = lane.getCells();
		for (int i = 0; i < cells.size(); i++) {
			cell = cells.get(i);
			if (!cell.isFree()) {
				vehicles++;
			}
			if (i % 2 == 1) {
				Assert.assertFalse(cell.isFree());
			}
			else {
				Assert.assertTrue(cell.isFree());
			}
		}
		Assert.assertEquals(5, vehicles);
		
		// check second lane, we should have the same amount of cars, but an
		// even distribution [1, null, 1, null, 1, null, 1, null, 1, null]
		vehicles = 0;
		lane = lanes.get(1);
		cells = lane.getCells();
		for (int i = 0; i < cells.size(); i++) {
			cell = cells.get(i);
			if (!cell.isFree()) {
				vehicles++;
			}
			if (i % 2 == 0) {
				Assert.assertFalse(cell.isFree());
			}
			else {
				Assert.assertTrue(cell.isFree());
			}
		}
		Assert.assertEquals(5, vehicles);
		
		// skip linger step
		
		model.stage4();
		
		// check second lane, we should have the same amount of cars, but an
		// even distribution [1, null, 1, null, 1, null, 1, null, 1, null]
		vehicles = 0;
		lane = lanes.get(0);
		cells = lane.getCells();
		for (int i = 0; i < cells.size(); i++) {
			cell = cells.get(i);
			if (!cell.isFree()) {
				vehicles++;
			}
			if (i % 2 == 0) {
				Assert.assertFalse(cell.isFree());
			}
			else {
				Assert.assertTrue(cell.isFree());
			}
		}
		Assert.assertEquals(5, vehicles);
		
		// check first lane, we should have the same amount of cars, but an even
		// distribution [null, 1, null, 1, null, 1, null, 1, null, 1]
		vehicles = 0;
		lane = lanes.get(1);
		cells = lane.getCells();
		for (int i = 0; i < cells.size(); i++) {
			cell = cells.get(i);
			if (!cell.isFree()) {
				vehicles++;
			}
			if (i % 2 == 1) {
				Assert.assertFalse(cell.isFree());
			}
			else {
				Assert.assertTrue(cell.isFree());
			}
		}
		Assert.assertEquals(5, vehicles);
		
	}
	
}
