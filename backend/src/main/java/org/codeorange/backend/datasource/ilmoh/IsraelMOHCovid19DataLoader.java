package org.codeorange.backend.datasource.ilmoh;

import java.net.URL;
import java.io.InputStream;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;

import org.codeorange.backend.api.data.Location;
import org.codeorange.backend.datasource.LocationDataLoader;

public class IsraelMOHCovid19DataLoader implements LocationDataLoader {
	
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
		InputStream is = null;

		try {
			is = new URL(API_URL).openStream();

			String rawResponse = IOUtils.toString(is);

			ApiResponse response = new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.readValue(rawResponse, ApiResponse.class);

			List<Location> result = new ArrayList<>();

			List<Feature> features = response.getFeatures();

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

				Location location = new Location(new Date(fromTime), new Date(toTime), lat, lon, 0.0);

				result.add(location);
			}

			System.out.println("---> Successfully loaded " + result.size() + " locations from Israel MOH API.");
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

}