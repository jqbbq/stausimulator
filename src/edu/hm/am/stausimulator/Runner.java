package edu.hm.am.stausimulator;

import java.util.ArrayList;
import java.util.List;

public class Runner implements Runnable {

	private Thread thread;
	private String name;

	private boolean paused;
	private int interval;

	private List<Runnable> runnables;

	public Runner(String name, int interval) {
		this.name = name;
		this.interval = interval;
		paused = true;
		runnables = new ArrayList<Runnable>();
	}

	public boolean addRunnable(Runnable runnable) {
		runnables.add(runnable);
		return true;
	}

	public boolean start() {
		if (thread == null) {
			thread = new Thread(this, name);
			thread.start();
			return true;
		}
		return false;
	}

	public boolean pause() {
		if (thread == null || paused) {
			return false;
		}

		paused = true;
		return true;
	}

	public boolean resume() {
		if (thread == null || !paused) {
			return false;
		}

		paused = false;
		return true;
	}

	public boolean stop() {
		if (thread == null) {
			return false;
		}

		paused = true;
		thread.interrupt();
		thread = null;
		return true;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getInterval() {
		return interval;
	}

	public boolean isRunning() {
		return thread != null && !paused;
	}

	@Override
	public void run() {
		try {
			while (true) {
				if (!paused) {
					for (Runnable runnable : runnables) {
						runnable.run();
					}
				}
				Thread.sleep(interval);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
