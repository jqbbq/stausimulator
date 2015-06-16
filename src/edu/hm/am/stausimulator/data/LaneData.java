package edu.hm.am.stausimulator.data;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.hm.am.stausimulator.model.Lane;

public class LaneData {
	
	private Lane											lane;
	private List<RoundData>									data;
	private Map<Integer, Map<String, Map<String, Object>>>	changes;
	
	public LaneData(Lane lane) {
		this.lane = lane;
		data = new ArrayList<>();
		changes = new HashMap<>();
	}
	
	public void add(RoundData data) {
		this.data.add(data);
	}
	
	public void addChange(Integer step, String changeName, Map<String, Object> changeData) {
		if (!changes.containsKey(step)) {
			changes.put(step, new HashMap<String, Map<String, Object>>());
		}
		Map<String, Map<String, Object>> change = changes.get(step);
		change.put(changeName, changeData);
	}
	
	public int getHeight() {
		return data.size();
	}
	
	public int getWidth() {
		return lane.getNumberOfCells();
	}
	
	public int getMax() {
		return lane.getMaxVelocity();
	}
	
	public int getSize() {
		return data.size();
	}
	
	public List<RoundData> getAll() {
		return data;
	}
	
	public List<RoundData> getLast(int size) {
		if (data.size() < size) {
			size = data.size();
		}
		List<RoundData> list = new ArrayList<>();
		for (int i = data.size() - size; i < data.size(); i++) {
			list.add(data.get(i).clone());
		}
		return list;
	}
	
	public void write(FileWriter writer) throws IOException {
		RoundData data;
		List<Integer> velocities;
		
		Integer cell;
		
		for (int r = 0; r < this.data.size(); r++) {
			// add row seperator
			if (r > 0) {
				writer.append("\n");
			}
			data = this.data.get(r);
			velocities = data.getVelocityPerCell();
			
			// writer.append(data.getFlow() + "");
			for (int c = 0; c < velocities.size(); c++) {
				cell = velocities.get(c);
				
				// add column seperator
				writer.append(";");
				
				// add cell value
				writer.append(cell == null ? "-" : cell + "");
			}
		}
	}
}
