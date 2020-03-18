package org.codeorange.backend.services.geocoding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.codeorange.backend.AppProperties;

/*package*/ class GoogleGeocodingService extends GeocodingService {

	private static final Logger logger = LoggerFactory.getLogger(GoogleGeocodingService.class);

	public static GeocodingService create() {
		return new GoogleGeocodingService();
	}

	private GoogleGeocodingService() {

	}

	@Override
	public ReverseGeocodingResult reverseGeocode(double lat, double lon) {
		return null;
	}

}
