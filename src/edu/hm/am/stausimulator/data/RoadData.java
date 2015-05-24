package edu.hm.am.stausimulator.data;

import java.util.ArrayList;
import java.util.List;

public class RoadData {

	private List<List<Integer>> data;

	public RoadData() {
		data = new ArrayList<List<Integer>>();
	}

	public boolean push(List<Integer> data) {
		this.data.add(data);
		return true;
	}
}
