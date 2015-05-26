package edu.hm.am.stausimulator.data;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.hm.am.stausimulator.model.Lane;

public class LaneData {

	private Lane lane;
	private List<List<Integer>> data;

	public LaneData(Lane lane) {
		this.lane = lane;
		data = new ArrayList<List<Integer>>();
	}

	public boolean add(List<Integer> Step) {
		return data.add(Step);
	}

	public int getHeight() {
		return data.size();
	}

	public int getWidth() {
		return lane.getCellsCount();
	}

	public int getMax() {
		return lane.getMaxSpeed();
	}

	public int getSize() {
		return data.size();
	}

	public List<List<Integer>> getAll() {
		return data;
	}

	public List<List<Integer>> getLast(int size) {
		if (data.size() < size) {
			size = data.size();
		}
		List<List<Integer>> list = new ArrayList<>();
		for (int i = data.size() - size; i < data.size(); i++) {
			list.add(new ArrayList<>(data.get(i)));
		}
		return list;
	}

	public void write(FileWriter writer) throws IOException {
		List<Integer> step;
		Integer cell;

		for (int r = 0; r < data.size(); r++) {
			step = data.get(r);
			if (r > 0) {
				writer.append("\n");
			}
			for (int c = 0; c < step.size(); c++) {
				cell = step.get(c);
				if (c > 0) {
					writer.append(";");
				}
				writer.append(cell == null ? "-" : cell + "");
			}
		}
	}
}
