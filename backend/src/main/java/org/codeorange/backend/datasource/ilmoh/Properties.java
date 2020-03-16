package org.codeorange.backend.datasource.ilmoh;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Properties {

	private long fromTime;
	private long toTime;

	@JsonProperty("Place")
	private String place;

	@JsonProperty("Comments")
	private String comments;

	public Properties() {

	}
	
	public Properties(long fromTime, long toTime, String place, String comments) {
		this.fromTime = fromTime;
		this.toTime = toTime;

		this.place = place;
		this.comments = comments;
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
	
	public String getPlace() {
		return this.place;
	}
	public void setPlace(String place) {
		this.place = place;
	}

	public String getComments() {
		return this.comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
}