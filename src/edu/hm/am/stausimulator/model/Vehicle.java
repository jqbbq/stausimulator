/*
 * 
 */
package edu.hm.am.stausimulator.model;

// TODO: Auto-generated Javadoc
/**
 * The Class Vehicle.
 */
public abstract class Vehicle {

	/** The speed. */
	private int maxspeed;
	private int speed;

	private boolean changedLane;
	private boolean lingered;

	public Vehicle() {
		speed = 0;
		maxspeed = 0;
		changedLane = false;
		lingered = false;
	}

	/**
	 * Instantiates a new vehicle.
	 *
	 * @param length the length
	 */
	public Vehicle(int max) {
		maxspeed = max;
		speed = 0;// random.nextInt(max + 1);
		changedLane = false;
		lingered = false;
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

	public boolean hasChangedLane() {
		return changedLane;
	}

	public void setChangedLane() {
		setChangedLane(true);
	}

	public void setChangedLane(boolean changed) {
		changedLane = changed;
	}
}
