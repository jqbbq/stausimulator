package edu.hm.am.stausimulator.view;


public class Runner implements Runnable {

	private Thread thread;
	private String name;
	private Runnable runnable;
	private boolean paused;
	private int interval;

	public Runner(String name, int interval, Runnable runnable) {
		this.name = name;
		this.interval = interval;
		this.runnable = runnable;
		this.paused = true;
	}

	public boolean start() {
		if(thread==null){
			thread = new Thread(this, name);
			thread.start();
			return true;
		}
		return false;
	}

	public boolean pause() {
		if(thread==null || paused){
			return false;
		}
		
		paused = true;
		return true;
	}

	public boolean resume() {
		if(thread==null || !paused){
			return false;
		}
		
		paused = false;
		return true;
	}

	public boolean stop() {
		if(thread==null){
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
	
	public boolean isRunning(){
		return thread!=null&&!paused;
	}

	@Override
	public void run() {
		try {
			while(true){
				if(!paused){
					runnable.run();
				}
				Thread.sleep(interval);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
