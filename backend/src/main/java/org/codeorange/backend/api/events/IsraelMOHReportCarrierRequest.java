package org.codeorange.backend.api.events;

import java.util.List;

import org.codeorange.backend.data.Location;

public class IsraelMOHReportCarrierRequest {

	private int patientCode;
	private List<Location> locations;

	public IsraelMOHReportCarrierRequest(int patientCode, List<Location> locations) {
		this.patientCode = patientCode;
		this.locations = locations;
	}
	
	public int getPatientCode() {
		return this.patientCode;
	}
	public void setPatientCode(int patientCode) {
		this.patientCode = patientCode;
	}
	
	public List<Location> getLocations() {
		return this.locations;
	}
	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

}
