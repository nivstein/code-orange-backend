package org.codeorange.backend.services.geocoding.google;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.codeorange.backend.AppProperties;
import org.codeorange.backend.services.geocoding.GeocodingService;
import org.codeorange.backend.services.geocoding.ReverseGeocoding;

public class GoogleGeocodingService extends GeocodingService {

	private static final Logger logger = LoggerFactory.getLogger(GoogleGeocodingService.class);

	private static final String REVERSE_GEOCODE_API_URL_FORMAT =
		"https://maps.googleapis.com/maps/api/geocode/json" +
			"?latlng=%f,%f" +
			"&key=%s" +
			"&language=iw" +
			"&result_type=street_address";

	private static final String STATUS_OK			  = "OK";
	private static final String LOCATION_TYPE_ROOFTOP = "ROOFTOP";

	public static GeocodingService create() {
		return new GoogleGeocodingService();
	}

	private GoogleGeocodingService() {

	}

	@Override
	public ReverseGeocoding reverseGeocode(double lat, double lon) {
		logger.info("About to reverse geocode ({}, {})...", lat, lon);

		InputStream is = null;

		try {
			String apiKey = AppProperties.get().get("code-orange.google-api-key");
			String url = String.format(REVERSE_GEOCODE_API_URL_FORMAT, lat, lon, apiKey);

			is = new URL(url).openStream();

			String rawResponse = IOUtils.toString(is, StandardCharsets.UTF_8);

			logger.info("Received {} bytes.", rawResponse.length());

			ReverseGeocodingResponse response = new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.readValue(rawResponse, ReverseGeocodingResponse.class);
			
			String status = response.getStatus();
			List<ReverseGeocodingResult> results = response.getResults();

			logger.info("Response successfully deserialized; status: {}; results: {}.",
				status, ((results != null) ? Integer.toString(results.size()) : "null"));

			if (!STATUS_OK.equalsIgnoreCase(status)) {

				logger.warn("Unable to reverse geocode ({}, {}); status: {}.", lat, lon, status);
				return null;
			}

			if (results != null) {
				for (ReverseGeocodingResult result : results) {

					String address = result.getFormattedAddress();

					if ((address == null) ||
						(address.isEmpty())) {
						continue;
					}

					ReverseGeocodingResultGeometry geometry = result.getGeometry();

					boolean isPrecise = ((geometry != null) &&
										 (LOCATION_TYPE_ROOFTOP.equalsIgnoreCase(geometry.getLocationType())));

					logger.info("Successfully reverse geocoded: ({}, {}) -> ({}; precise: {}).",
						lat, lon, address, isPrecise);

					return new ReverseGeocoding(address, isPrecise);
				}
			}

			logger.warn("Unable to reverse geocode ({}, {}); no address found.", lat, lon);

			return null;
		} catch (Exception e) {
			logger.error("Error reverse geocoding ({}, {}).", lat, lon, e);

			return null;
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

}
