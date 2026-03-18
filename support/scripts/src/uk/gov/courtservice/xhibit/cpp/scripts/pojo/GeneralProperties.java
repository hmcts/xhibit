package uk.gov.courtservice.xhibit.cpp.scripts.pojo;

public class GeneralProperties {

	private int sleepTimePerLoop;
	private int numThreads;

	public int getSleepTimePerLoop() {
		return sleepTimePerLoop;
	}

	public void setSleepTimePerLoop(int sleepTimePerLoop) {
		this.sleepTimePerLoop = sleepTimePerLoop;
	}
	
	public int getNumThreads() {
		return numThreads;
	}

	public void setNumThreads(int numThreads) {
		this.numThreads = numThreads;
	}

	@Override
	public String toString() {
		return "GeneralProperties [sleepTimePerLoop=" + sleepTimePerLoop + ", numThreads=" + numThreads + "]";
	}

}
