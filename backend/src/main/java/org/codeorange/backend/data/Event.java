package org.codeorange.backend.data;

public enum Event {
	
	COVID_19("covid-19");

	private final String id;

	Event(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
	
}
