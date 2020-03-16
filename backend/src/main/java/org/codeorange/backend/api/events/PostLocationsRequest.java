package org.codeorange.backend.api.events;

import java.util.List;

import org.codeorange.backend.data.Location;

public class PostLocationsRequest {

	private String patientStatus;
	private String country;

	private List<Location> locations;

	public PostLocationsRequest(String patientStatus, String country, List<Location> locations) {
		this.patientStatus = patientStatus;
		this.country = country;

		this.locations = locations;
	}

	public String getPatientStatus() {
		return this.patientStatus;
	}
	public void setPatientStatus(String patientStatus) {
		this.patientStatus = patientStatus;
	}

	public String getCountry() {
		return this.country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	public List<Location> getLocations() {
		return this.locations;
	}
	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

}
