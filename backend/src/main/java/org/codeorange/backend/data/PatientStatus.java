package org.codeorange.backend.data;

public enum PatientStatus {

	CARRIER("carrier");

	private final String id;

	PatientStatus(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}
	
}