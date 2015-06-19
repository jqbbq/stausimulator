package edu.hm.am.stausimulator.data;

import java.util.ArrayList;
import java.util.List;

public class RoundData {
	
	private List<Integer>	velocityPerCell;
	
	private double			averageVelocity;
	private double			flow;
	
	public RoundData() {
		averageVelocity = 0;
		flow = 0;
		velocityPerCell = new ArrayList<Integer>();
	}
	
	public RoundData(List<Integer> velocities) {
		velocityPerCell = velocities;
		averageVelocity = calcAvgVelocity(velocityPerCell);
		flow = 0;
	}
	
	public List<Integer> getVelocityPerCell() {
		return velocityPerCell;
	}
	
	public void setVelocityPerCell(List<Integer> velocityPerCell) {
		this.velocityPerCell = velocityPerCell;
		averageVelocity = calcAvgVelocity(velocityPerCell);
	}
	
	public double getAverageVelocity() {
		return averageVelocity;
	}
	
	public double getFlow() {
		return flow;
	}
	
	public void setFlow(double flow) {
		this.flow = flow;
	}
	
	private double calcAvgVelocity(List<Integer> values) {
		double vehicles = 0.0;
		double sum = 0.0;
		for (Integer value : values) {
			if (value != null) {
				vehicles++;
				sum += Math.abs(value.intValue());
			}
		}
		return sum / vehicles;
	}
	
	@Override
	protected RoundData clone() {
		RoundData round = new RoundData();
		
		round.setVelocityPerCell(getVelocityPerCell());
		round.setFlow(getFlow());
		
		return round;
	}
}
