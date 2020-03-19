package org.codeorange.backend.services.geocoding;

public class ReverseGeocoding {

	private final String address;
	private final boolean isPrecise;

	public ReverseGeocoding(String address, boolean isPrecise) {
		this.address = address;
		this.isPrecise = isPrecise;
	}

	public String getAddress() {
		return this.address;
	}

	public boolean isPrecise() {
		return this.isPrecise;
	}
	
}
