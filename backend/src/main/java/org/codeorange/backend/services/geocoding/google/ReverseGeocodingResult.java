package org.codeorange.backend.services.geocoding.google;

import com.fasterxml.jackson.annotation.JsonProperty;

/*pacage*/ class ReverseGeocodingResult {

	@JsonProperty("formatted_address")
	private String formattedAddress;

	private ReverseGeocodingResultGeometry geometry;

	public ReverseGeocodingResult() {

	}

	public ReverseGeocodingResult(String formattedAddress, ReverseGeocodingResultGeometry geometry) {
		this.formattedAddress = formattedAddress;
		this.geometry = geometry;
	}

	public String getFormattedAddress() {
		return this.formattedAddress;
	}
	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

	public ReverseGeocodingResultGeometry getGeometry() {
		return this.geometry;
	}
	public void setGeometry(ReverseGeocodingResultGeometry geometry) {
		this.geometry = geometry;
	}
	
}
