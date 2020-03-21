package org.codeorange.backend.datasource.ilmoh;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.codeorange.backend.data.Location;
import org.codeorange.backend.datasource.LocationDataLoader;

public class IsraelMOHCovid19DataLoader implements LocationDataLoader {
	
	private static final Logger logger = LoggerFactory.getLogger(IsraelMOHCovid19DataLoader.class);

	private static final String API_URL = "https://services5.arcgis.com/" +
		"dlrDjz89gx9qyfev/ArcGIS/rest/services/Corona_Exposure_View/FeatureServer/0/query" +
		"?where=1%3D1&objectIds=&time=&geometry=&geometryType=esriGeometryEnvelope&inSR=" +
		"&spatialRel=esriSpatialRelIntersects&resultType=none&distance=0.0&units=esriSRUnit_Meter" +
		"&returnGeodetic=false&outFields=*&returnGeometry=true&featureEncoding=esriDefault" +
		"&multipatchOption=xyFootprint&maxAllowableOffset=&geometryPrecision=&outSR=" +
		"&datumTransformation=&applyVCSProjection=false&returnIdsOnly=false&returnUniqueIdsOnly=false" +
		"&returnCountOnly=false&returnExtentOnly=false&returnQueryGeometry=false" +
		"&returnDistinctValues=false&cacheHint=false&orderByFields=&groupByFieldsForStatistics=" +
		"&outStatistics=&having=&resultOffset=&resultRecordCount=&returnZ=false&returnM=false" +
		"&returnExceededLimitFeatures=true&quantizationParameters=&sqlFormat=none&f=pgeojson&token=";

	public static LocationDataLoader create() {
		return new IsraelMOHCovid19DataLoader();
	}

	private IsraelMOHCovid19DataLoader() {

	}

	@Override
	public List<Location> load() {
		logger.info("About to load Israel MOH COVID-19 location data...");

		InputStream is = null;

		try {
			is = new URL(API_URL).openStream();

			String rawResponse = IOUtils.toString(is, StandardCharsets.UTF_8);

			logger.info("Received {} bytes.", rawResponse.length());

			ApiResponse response = new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.readValue(rawResponse, ApiResponse.class);

			List<Location> result = new ArrayList<>();

			List<Feature> features = response.getFeatures();
			
			logger.info("Response successfully deserialized, contains {} features.", features.size());

			for (Feature feature : features) {
				Geometry geometry = feature.getGeometry();
				Properties properties = feature.getProperties();

				if ((geometry == null) ||
					(properties == null)) {
					continue;
				}

				List<Double> coordinates = geometry.getCoordinates();

				if ((coordinates == null) ||
					(coordinates.size() != 2) ||
					(coordinates.get(0) == null) ||
					(coordinates.get(1) == null)) {
					continue;
				}

				double lat = coordinates.get(1);
				double lon = coordinates.get(0);

				long fromTime = properties.getFromTime();
				long toTime = properties.getToTime();

				if ((fromTime == 0l) ||
					(toTime == 0l)) {
					continue;
				}

				String name = properties.getPlace();
				String comments = properties.getComments();

				result.add(new Location(new Date(fromTime), new Date(toTime), lat, lon, 0.0, name, comments));
			}

			logger.info("Done. Resolved {} locations out of {} features.", result.size(), features.size());

			return result;
		} catch (Exception e) {
			logger.error("Error loading Israel MOH data.", e);

			return null;
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

}