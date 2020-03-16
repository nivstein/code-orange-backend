package org.codeorange.backend.api.events;

import java.util.List;

import org.codeorange.backend.data.Location;

public class GetLocationsResponse {

	private List<Location> locations;

	public GetLocationsResponse(List<Location> locations) {
		this.locations = locations;
	}

	public List<Location> getLocations() {
		return this.locations;
	}
	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

}
