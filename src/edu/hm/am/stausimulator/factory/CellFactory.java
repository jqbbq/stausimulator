package edu.hm.am.stausimulator.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.hm.am.stausimulator.model.Cell;
import edu.hm.am.stausimulator.model.Vehicle;

public class CellFactory {
	public enum Distribution {
		EVEN, START, CENTER, END, RANDOM
	}
	
	public static List<Cell> createCells(int count, List<Vehicle> vehicles, Distribution distribution) {
		List<Cell> cells = new ArrayList<Cell>();
		
		for (int i = 0; i < count; i++) {
			cells.add(new Cell());
		}
		
		switch (distribution) {
			case EVEN:
				evenDistribution(cells, vehicles);
				break;
			case START:
				startDistribution(cells, vehicles);
				break;
			case CENTER:
				centerDistribution(cells, vehicles);
				break;
			case END:
				endDistribution(cells, vehicles);
				break;
			case RANDOM:
				randomDistribution(cells, vehicles);
				break;
		}
		
		return cells;
	}
	
	private static void evenDistribution(List<Cell> cells, List<Vehicle> vehicles) {
		if (vehicles.size() > 0) {
			int step = cells.size() / vehicles.size();
			double mod = (cells.size() * 1.0) / vehicles.size() - step;
			
			int position = 0;
			double x = 0;
			
			for (Vehicle vehicle : vehicles) {
				cells.get(position).setVehicle(vehicle);
				
				position += step;
				x += mod;
				if (x > 1) {
					position++;
					x -= 1;
				}
			}
		}
	}
	
	private static void startDistribution(List<Cell> cells, List<Vehicle> vehicles) {
		for (int i = 0; i < vehicles.size(); i++) {
			cells.get(i).setVehicle(vehicles.get(i));
		}
	}
	
	private static void centerDistribution(List<Cell> cells, List<Vehicle> vehicles) {
		
	}
	
	private static void endDistribution(List<Cell> cells, List<Vehicle> vehicles) {
		for (int i = 0; i < vehicles.size(); i++) {
			cells.get(cells.size() - (i + 1)).setVehicle(vehicles.get(i));
		}
	}
	
	private static void randomDistribution(List<Cell> cells, List<Vehicle> vehicles) {
		final Random random = new Random();
		
		Cell cell;
		for (Vehicle vehicle : vehicles) {
			while (true) {
				cell = cells.get(random.nextInt(cells.size()));
				if (cell.isFree()) {
					cell.setVehicle(vehicle);
					break;
				}
			}
		}
	}
}
