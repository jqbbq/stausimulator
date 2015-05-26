package edu.hm.am.stausimulator;

import java.util.ArrayList;
import java.util.List;

public class Runner implements Runnable {

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
		running = true;
		thread = new Thread(this, name);
		thread.start();
	}

	public void stop() {
		running = false;
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