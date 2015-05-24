package edu.hm.am.stausimulator.data;

import java.util.ArrayList;
import java.util.List;

public class LaneData {

	private int height;

	private List<List<Integer>> data;

	public LaneData(int height) {
		this.height = height;
		data = new ArrayList<List<Integer>>();
	}

	public boolean add(List<Integer> row) {
		return data.add(row);
	}

	public int getHeight() {
		return height;
	}

	public int getSize() {
		return data.size();
	}

	public List<List<Integer>> getLast(int size) {
		if (data.size() < size) {
			size = data.size();
		}
		return data.subList(data.size() - size, data.size());
	}
}
