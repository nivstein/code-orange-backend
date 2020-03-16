package org.codeorange.backend.data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.codeorange.backend.util.DateUtil;

public class Location {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.ISO8601_FORMAT)
	private Date startTime;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.ISO8601_FORMAT)
	private Date endTime;

	private double lat;
	private double lon;

	private double radius;

	private String name;
	private String comments;

	public Location(Date startTime, Date endTime, double lat, double lon, double radius,
		String name, String comments) {

		this.startTime = startTime;
		this.endTime = endTime;

		this.lat = lat;
		this.lon = lon;

		this.radius = radius;

		this.name = name;
		this.comments = comments;
	}

	public Date getStartTime() {
		return this.startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public double getLat() {
		return this.lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return this.lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getRadius() {
		return this.radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return this.comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

}