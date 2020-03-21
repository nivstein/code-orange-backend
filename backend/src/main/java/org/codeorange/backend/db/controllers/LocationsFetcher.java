package org.codeorange.backend.db.controllers;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.codeorange.backend.data.Location;
import org.codeorange.backend.db.queries.GetLocationsQueryBuilder;
import org.codeorange.backend.db.util.DbUtil;
import org.codeorange.backend.db.util.HibernateUtil;

public class LocationsFetcher {

	private static final Logger logger = LoggerFactory.getLogger(LocationsFetcher.class);

	public static List<Location> fetch(String eventId, String minEntryTime, String patientStatus, String country) {

		logger.info("About to fetch locations for [{}; {}; {}; {}].", eventId, minEntryTime, patientStatus, country);

		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();

			Transaction transaction = session.beginTransaction();

			List<Location> locations = new ArrayList<>();

			List<String> tableNames = DbUtil.getTablesByCountry(country);

			logger.info("Fetching from tables: {}.", tableNames);

			for (String tableName : tableNames) {
				Query query = GetLocationsQueryBuilder.build(session, tableName, eventId, minEntryTime, patientStatus, country);

				List<Object[]> rawLocations = query.list();

				logger.info("Retrieved {} locations from table {}.", rawLocations.size(), tableName);

				for (Object[] rawLocation : rawLocations) {
					locations.add(new Location(
						new Date(((BigInteger)rawLocation[0]).longValue()),
						new Date(((BigInteger)rawLocation[1]).longValue()),
						(Double)rawLocation[2],
						(Double)rawLocation[3],
						(Double)rawLocation[4],
						(String)rawLocation[5],
						(String)rawLocation[6]));
				}
			}

			transaction.commit();

			logger.info("Done. Returning a total of {} locations.", locations.size());

			return locations;
		} catch (ParseException e) {
			logger.error("Error fetching locations.", e);

			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}
