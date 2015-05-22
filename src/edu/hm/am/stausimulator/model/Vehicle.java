/*
 * 
 */
package edu.hm.am.stausimulator.model;

import java.util.Random;

import edu.hm.am.stausimulator.Configuration;
import edu.hm.am.stausimulator.Property;

// TODO: Auto-generated Javadoc
/**
 * The Class Vehicle.
 */
public abstract class Vehicle {

	private static final Random random = new Random();

	/** The speed. */
	private int speed;

	/**
	 * Instantiates a new vehicle.
	 *
	 * @param length
	 *            the length
	 */
	public Vehicle() {
		speed = random.nextInt(Configuration.getProperty(Property.MAX_SPEED).intValue());
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
	 * @param speed
	 *            the new speed
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public boolean speedUp() {
		if (speed < Configuration.getProperty(Property.MAX_SPEED).intValue()) {
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
}
