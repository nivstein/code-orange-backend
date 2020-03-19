package org.codeorange.backend.datasource.ilmoh;

import java.util.List;

/*package*/ class Geometry {

	private List<Double> coordinates;

	public Geometry() {

	}
	
	public Geometry(List<Double> coordinates) {
		this.coordinates = coordinates;
	}

	public List<Double> getCoordinates() {
		return this.coordinates;
	}
	public void setCoordinates(List<Double> coordinates) {
		this.coordinates = coordinates;
	}
	
}