package net.timothyhahn.multipong;

import java.util.Calendar;

public class Timer  implements Runnable {
	private volatile boolean finished;
	private Calendar timer;
	private Calendar until;
	private boolean running;
	
	public Timer(Calendar until){
		timer = Calendar.getInstance();
		this.until = until;
		finished = false;
		running = true;
	}
	
	public void reset(Calendar until){
		this.until = until;
		this.finished = false;
	}
	
	public boolean isFinished(){
		return finished;
	}
	
	public void stop() {
		running = false;
		Thread.currentThread().interrupt();
	}
	
	@Override
	public void run() {
		while(running) {
			if(timer.after(until)){
				finished = true;
			}
			timer = Calendar.getInstance();
		}
	}
}
