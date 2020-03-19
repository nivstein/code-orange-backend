package org.codeorange.backend.services.geocoding;

import org.codeorange.backend.services.geocoding.google.GoogleGeocodingService;

public abstract class GeocodingService {

	private static volatile GeocodingService instance = null;

	public static GeocodingService get() {
		if (instance == null) {
			synchronized (GeocodingService.class) {
				if (instance == null) {
					instance = create();
				}
			}
		}

		return instance;
	}

	private static GeocodingService create() {
		return GoogleGeocodingService.create();
	}

	public abstract ReverseGeocoding reverseGeocode(double lat, double lon);

}
