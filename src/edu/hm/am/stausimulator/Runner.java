package edu.hm.am.stausimulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Runner extends Observable implements Runnable {

	private int steps;
	private int run;
	private int interval;
	private boolean running;

	private String name;

	private Thread thread;
	private List<Runnable> runnables;

	public Runner(String name, int interval) {
		this.name = name;
		this.interval = interval;

		running = false;
		runnables = new ArrayList<Runnable>();
	}

	public boolean addRunnable(Runnable runnable) {
		runnables.add(runnable);
		return true;
	}

	public boolean removeRunnable(Runnable runnable) {
		return runnables.remove(runnable);
	}

	public void start() {
		start(0);
	}

	public void start(int steps) {
		this.steps = steps;
		run = 0;
		running = true;
		thread = new Thread(this, name);
		thread.start();
		setChanged();
		notifyObservers("Start");
	}

	public void stop() {
		running = false;
		setChanged();
		notifyObservers("Stop");
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getInterval() {
		return interval;
	}

	public boolean isRunning() {
		return running;
	}

	@Override
	public void run() {
		try {
			while (running) {
				run++;
				if (steps > 0 && run == steps) {
					stop();
				}
				for (Runnable runnable : runnables) {
					runnable.run();
				}
				Thread.sleep(interval);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}