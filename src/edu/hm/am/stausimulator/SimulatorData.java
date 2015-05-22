package edu.hm.am.stausimulator;

import java.util.ArrayList;
import java.util.List;

public class SimulatorData {

	private List<List<Integer>> data;

	public SimulatorData() {
		data = new ArrayList<List<Integer>>();
	}

	public boolean push(List<Integer> data) {
		this.data.add(data);
		return true;
	}
}
