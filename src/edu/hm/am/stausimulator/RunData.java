package edu.hm.am.stausimulator;

import java.util.ArrayList;
import java.util.List;

public class RunData {

	private List<List<Integer>> data;

	public RunData() {
		data = new ArrayList<List<Integer>>();
	}

	public boolean push(List<Integer> data) {
		this.data.add(data);
		return true;
	}
}
