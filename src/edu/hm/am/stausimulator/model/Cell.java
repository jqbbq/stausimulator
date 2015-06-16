/*
 * 
 */
package edu.hm.am.stausimulator.model;

// TODO: Auto-generated Javadoc
/**
 * The Class Cell.
 */
public class Cell {
	
	/** The vehicle. */
	private Vehicle	vehicle;
	
	/**
	 * Instantiates a new cell.
	 */
	public Cell() {
	}
	
	/**
	 * Instantiates a new cell.
	 *
	 * @param vehicle the vehicle
	 */
	public Cell(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	public Vehicle getVehicle() {
		return vehicle;
	}
	
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	public boolean isFree() {
		return vehicle == null;
	}
	
}
