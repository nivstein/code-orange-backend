package org.codeorange.backend.datasource.ilmoh;

import java.util.List;

/*package*/ class ApiResponse {

	private List<Feature> features;

	public ApiResponse() {

	}
	
	public ApiResponse(List<Feature> features) {
		this.features = features;
	}

	public List<Feature> getFeatures() {
		return this.features;
	}
	public void setFeatures(List<Feature> features) {
		this.features = features;
	}
}