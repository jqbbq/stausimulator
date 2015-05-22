/*
 * 
 */
package edu.hm.am.stausimulator.model;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Road.
 */
public class Road {

	/** The lanes. */
	private final List<Lane> lanes;

	/**
	 * Instantiates a new road.
	 */
	public Road() {
		this(1);
	}

	/**
	 * Instantiates a new road.
	 *
	 * @param lanes
	 *            the lanes
	 */
	public Road(int lanes) {
		this.lanes = new ArrayList<Lane>(lanes);
		for (int i = 0; i < lanes; i++) {
			this.lanes.add(new Lane());
		}
	}

	public List<Lane> getLanes() {
		return lanes;
	}

	@Override
	public String toString() {
		// TODO implement usefull
		return "Road with " + lanes.size() + " lanes.";
	}
}
