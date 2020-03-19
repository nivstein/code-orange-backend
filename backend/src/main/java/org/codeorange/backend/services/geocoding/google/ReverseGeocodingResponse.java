package org.codeorange.backend.services.geocoding.google;

import java.util.List;

/*pacage*/ class ReverseGeocodingResponse {

	private String status;
	private List<ReverseGeocodingResult> results;

	public ReverseGeocodingResponse() {

	}

	public ReverseGeocodingResponse(String status, List<ReverseGeocodingResult> results) {
		this.status = status;
		this.results = results;
	}

	public String getStatus() {
		return this.status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public List<ReverseGeocodingResult> getResults() {
		return this.results;
	}
	public void setResults(List<ReverseGeocodingResult> results) {
		this.results = results;
	}

}
