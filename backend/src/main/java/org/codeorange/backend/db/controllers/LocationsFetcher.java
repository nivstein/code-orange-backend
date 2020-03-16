package org.codeorange.backend.db.controllers;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.codeorange.backend.data.Location;
import org.codeorange.backend.db.queries.GetLocationsQueryBuilder;
import org.codeorange.backend.db.util.DbUtil;
import org.codeorange.backend.db.util.HibernateUtil;

public class LocationsFetcher {

	public static List<Location> fetch(String eventId, String minEntryTime, String patientStatus, String country) {
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();

			Transaction transaction = session.beginTransaction();

			List<Location> locations = new ArrayList<>();

			List<String> tableNames = DbUtil.getTablesByCountry(country);

			for (String tableName : tableNames) {
				Query query = GetLocationsQueryBuilder.build(session, tableName, eventId, minEntryTime, patientStatus, country);

				List<Object[]> rawLocations = query.list();

				for (Object[] rawLocation : rawLocations) {
					locations.add(new Location(
						new Date(((BigInteger)rawLocation[0]).longValue()),
						new Date(((BigInteger)rawLocation[1]).longValue()),
						(Double)rawLocation[2],
						(Double)rawLocation[3],
						(Double)rawLocation[4]));
				}
			}

			transaction.commit();

			return locations;
		} catch (ParseException e) {
			e.printStackTrace();

			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}
