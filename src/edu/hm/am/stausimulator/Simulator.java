/*
 * 
 */
package edu.hm.am.stausimulator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import edu.hm.am.stausimulator.model.Road;

// TODO: Auto-generated Javadoc
/**
 * The Class Simulator.
 */
public class Simulator extends Observable {

	private static Simulator instance;

	private Runner runner;

	private List<Road> roads;

	public static Simulator getInstance() {
		if (instance == null) {
			instance = new Simulator();
		}
		return instance;
	}

	public List<Road> getRoads() {
		return roads;
	}

	public Road getRoad(int index) {
		if (index >= roads.size()) {
			return null;
		}
		return roads.get(index);
	}

	public boolean addRoad(Road road) {
		return roads.add(road);
	}

	public boolean removeRoad(int index) {
		if (index < roads.size()) {
			roads.remove(index);
			return true;
		}
		return false;
	}

	/*
	 * public boolean addListener(Runnable runnable) { return
	 * runner.addRunnable(runnable); }
	 * 
	 * public boolean removeListener(Runnable runnable) { return
	 * runner.removeRunnable(runnable); }
	 */

	public void start() {
		runner.start();
		setChanged();
		notifyObservers("Start");
	}

	public void stop() {
		runner.stop();
		setChanged();
		notifyObservers("Stop");
	}

	public boolean isRunning() {
		return runner.isRunning();
	}

	public void setInterval(int interval) {
		runner.setInterval(interval);
		setChanged();
		notifyObservers("Interval changed");
	}

	public void nextStep() {
		for (Road road : roads) {
			road.nextStep();
		}
		setChanged();
		notifyObservers("Step");
	}

	public void save(File directory) {
		try {
			for (Road road : roads) {
				road.save(directory);
			}
		} catch (IOException e) {
			// TODO remove
			e.printStackTrace();
		}
	}

	public void reset() {

	}

	private Simulator() {
		roads = new ArrayList<Road>();
		roads.add(new Road(1));
		runner = new Runner("Main Runner", Defaults.INTERVAL);
		runner.addRunnable(new Runnable() {
			@Override
			public void run() {
				nextStep();
			}
		});
	}

}
