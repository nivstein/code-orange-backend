package org.codeorange.backend.datasource.ilmoh;

public class Properties {

	private long fromTime;
	private long toTime;

	public Properties() {

	}
	
	public Properties(long fromTime, long toTime) {
		this.fromTime = fromTime;
		this.toTime = toTime;
	}

	public long getFromTime() {
		return this.fromTime;
	}
	public void setFromTime(long fromTime) {
		this.fromTime = fromTime;
	}

	public long getToTime() {
		return this.toTime;
	}
	public void setToTime(long toTime) {
		this.toTime = toTime;
	}
	
}