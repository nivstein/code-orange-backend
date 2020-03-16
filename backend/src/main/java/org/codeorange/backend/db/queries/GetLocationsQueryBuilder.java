package org.codeorange.backend.db.queries;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import org.codeorange.backend.util.DateUtil;

public class GetLocationsQueryBuilder {

	public static Query build(Session session, String tableName,
		String eventId, String minEntryTime, String patientStatus, String country) throws ParseException {

		StringBuilder sb = new StringBuilder();

		Map<Integer, String> namedParams = new HashMap<>();
		int currentParamIndex = 0;

		sb.append("SELECT from_timestamp, to_timestamp, lat, lon, radius ")
		  .append("FROM " + tableName + " ")
		  .append("WHERE event_id = ?" + (++currentParamIndex) + " ");

		namedParams.put(currentParamIndex, eventId);

		if (minEntryTime != null) {
			long minEntryTimestamp = DateUtil.iso8601ToTimestamp(minEntryTime);

			sb.append("AND received_timestamp > ?" + (++currentParamIndex) + " ");
			namedParams.put(currentParamIndex, Long.toString(minEntryTimestamp));
		}
		
		if (patientStatus != null) {
			sb.append("AND patient_status = ?" + (++currentParamIndex) + " ");
			namedParams.put(currentParamIndex, patientStatus);
		}

		if (country != null) {
			sb.append("AND country = ?" + (++currentParamIndex) + " ");
			namedParams.put(currentParamIndex, country);
		}

		Query query = session.createNativeQuery(sb.toString());

		for (Map.Entry<Integer, String> namedParam : namedParams.entrySet()) {
			query.setParameter(namedParam.getKey(), namedParam.getValue());
		}
		
		return query;
	}

}
