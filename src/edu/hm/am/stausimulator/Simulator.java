/*
 * 
 */
package edu.hm.am.stausimulator;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.hm.am.stausimulator.model.Road;

// TODO: Auto-generated Javadoc
/**
 * The Class Simulator.
 */
public class Simulator extends Observable {
	
	private static Simulator	instance;
	
	private Runner				runner;
	
	private List<Road>			roads;
	
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
	
	public void start() {
		runner.start();
	}
	
	public void start(int steps) {
		runner.start(steps);
	}
	
	public void stop() {
		runner.stop();
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
			DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH.mm");
			File parent = new File(directory, df.format(new Date()) + " Nagel-Schreckenberg-Data");
			File roadDir;
			
			parent.mkdir();
			
			Road road;
			for (int i = 0; i < roads.size(); i++) {
				road = roads.get(i);
				roadDir = new File(parent, "Road " + (i + 1));
				roadDir.mkdir();
				
				road.save(roadDir);
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
		roads.add(new Road());
		runner = new Runner("Main Runner", Defaults.INTERVAL);
		runner.addRunnable(new Runnable() {
			@Override
			public void run() {
				nextStep();
			}
		});
		runner.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				instance.setChanged();
				instance.notifyObservers(arg);
			}
		});
	}
	
}
