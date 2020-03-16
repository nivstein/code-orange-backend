package org.codeorange.backend.db.queries;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import org.codeorange.backend.api.data.Location;
import org.codeorange.backend.util.DateUtil;

public class InsertLocationsQueryBuilder {

	public static Query build(Session session, String tableName,
		String eventId, String patientStatus, String country, long receivedTimestamp,
		List<Location> locations) {

		StringBuilder sb = new StringBuilder();

		Map<Integer, String> namedParams = new HashMap<>();
		int currentParamIndex = 0;

		sb.append("INSERT INTO `" + tableName + "` (")
		  .append("`event_id`, `country`, `from_timestamp`, `to_timestamp`, ") // device ID remains null (FFU)
		  .append("`lat`, `lon`, `radius`, `patient_status`, `received_timestamp`) ")
		  .append("VALUES ");

		for (int i = 0; i < locations.size(); i++) {
			Location location = locations.get(i);

			sb.append("(?" + (++currentParamIndex) + ", ");
			namedParams.put(currentParamIndex, eventId);

			sb.append("?" + (++currentParamIndex) + ", ");
			namedParams.put(currentParamIndex, country);
			
			sb.append("?" + (++currentParamIndex) + ", ");
			namedParams.put(currentParamIndex, Long.toString(location.getStartTime().getTime()));

			sb.append("?" + (++currentParamIndex) + ", ");
			namedParams.put(currentParamIndex, Long.toString(location.getEndTime().getTime()));
			
			sb.append("?" + (++currentParamIndex) + ", ");
			namedParams.put(currentParamIndex, Double.toString(location.getLat()));

			sb.append("?" + (++currentParamIndex) + ", ");
			namedParams.put(currentParamIndex, Double.toString(location.getLon()));

			sb.append("?" + (++currentParamIndex) + ", ");
			namedParams.put(currentParamIndex, Double.toString(location.getRadius()));

			sb.append("?" + (++currentParamIndex) + ", ");
			namedParams.put(currentParamIndex, patientStatus);

			sb.append("?" + (++currentParamIndex) + ")");
			namedParams.put(currentParamIndex, Long.toString(receivedTimestamp));

			sb.append((i == locations.size() - 1) ? "; " : ", ");
		}

		Query query = session.createNativeQuery(sb.toString());

		for (Map.Entry<Integer, String> namedParam : namedParams.entrySet()) {
			query.setParameter(namedParam.getKey(), namedParam.getValue());
		}
		
		return query;
	}

}
