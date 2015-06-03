package edu.hm.am.stausimulator.data;

import java.util.List;

public class RoundData {

	private List<Integer> velocityPerCell;

	private int averageVelocity;
	private int averageFlow;

	public RoundData() {
	}

	public RoundData(List<Integer> velocities) {
		velocityPerCell = velocities;
		averageVelocity = calcAvgVelocity(velocityPerCell);
		averageFlow = 0;
	}

	public List<Integer> getVelocityPerCell() {
		return velocityPerCell;
	}

	public void setVelocityPerCell(List<Integer> velocityPerCell) {
		this.velocityPerCell = velocityPerCell;
		averageVelocity = calcAvgVelocity(velocityPerCell);
	}

	public int getAverageVelocity() {
		return averageVelocity;
	}

	public int getAverageFlow() {
		return averageFlow;
	}

	public void setAverageFlow(int averageFlow) {
		this.averageFlow = averageFlow;
	}

	private int calcAvgVelocity(List<Integer> values) {
		int vehicles = 0;
		int sum = 0;
		for (Integer value : values) {
			if (value != null) {
				vehicles++;
				sum += value.intValue();
			}
		}
		return sum / vehicles;
	}

	@Override
	protected RoundData clone() {
		RoundData d = new RoundData();

		return d;
	}
}
