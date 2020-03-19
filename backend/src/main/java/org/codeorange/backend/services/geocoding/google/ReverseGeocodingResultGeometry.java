package org.codeorange.backend.services.geocoding.google;

import com.fasterxml.jackson.annotation.JsonProperty;

/*pacage*/ class ReverseGeocodingResultGeometry {

	@JsonProperty("location_type")
	private String locationType;

	public ReverseGeocodingResultGeometry() {

	}

	public ReverseGeocodingResultGeometry(String locationType) {
		this.locationType = locationType;
	}

	public String getLocationType() {
		return this.locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	
}
