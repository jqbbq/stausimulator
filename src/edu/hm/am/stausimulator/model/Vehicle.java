/*
 * 
 */
package edu.hm.am.stausimulator.model;

import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * The Class Vehicle.
 */
public abstract class Vehicle {

	private static final Random random = new Random();

	/** The speed. */
	private int maxspeed;
	private int speed;

	private boolean lingered;

	/**
	 * Instantiates a new vehicle.
	 *
	 * @param length the length
	 */
	public Vehicle(int maxspeed) {
		this.maxspeed = maxspeed;
		speed = random.nextInt(maxspeed);
	}

	/**
	 * Gets the speed.
	 *
	 * @return the speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Sets the speed.
	 *
	 * @param speed the new speed
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void setLingered(boolean lingered) {
		this.lingered = lingered;
	}

	public boolean speedUp() {
		if (speed < maxspeed) {
			speed++;
			return true;
		}
		return false;
	}

	public boolean slowDown() {
		if (speed > 0) {
			speed--;
			return true;
		}
		return false;
	}

	public boolean linger() {
		lingered = true;
		return slowDown();
	}

	public boolean hasLingered() {
		return lingered;
	}
}
