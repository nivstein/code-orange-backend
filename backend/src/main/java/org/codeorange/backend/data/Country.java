package org.codeorange.backend.data;

public enum Country {
	
	ISRAEL("il");

	private final String code;

	Country(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
	
}
