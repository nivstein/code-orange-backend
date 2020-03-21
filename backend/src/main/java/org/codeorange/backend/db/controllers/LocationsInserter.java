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
import org.codeorange.backend.db.queries.InsertLocationsQueryBuilder;
import org.codeorange.backend.db.util.HibernateUtil;

public class LocationsInserter {

	private static final Logger logger = LoggerFactory.getLogger(LocationsInserter.class);

	private static final int BATCH_SIZE = 50; //NNNTODO move to config

	public static void insert(String tableName, String eventId, String patientStatus,
		String country, long receivedTimestamp, List<Location> locations) {
		
		logger.info("About to insert {} locations into table {}, for [{}; {}; {}; {}].",
			locations.size(), tableName, eventId, patientStatus, country, receivedTimestamp);

		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			
			Transaction transaction = session.beginTransaction();

			List<List<Location>> locationBatches = splitBatches(locations, BATCH_SIZE);

			logger.info("Split a total of {} locations into {} batches (batch size: {}).",
				locations.size(), locationBatches.size(), BATCH_SIZE);

			for (int i = 0; i < locationBatches.size(); i++) {
				List<Location> locationBatch = locationBatches.get(i);

				logger.info("About to insert batch {} out of {}...", i + 1, locationBatches.size());

				Query query = InsertLocationsQueryBuilder.build(session, tableName,
					eventId, patientStatus, country, receivedTimestamp, locationBatch);

				query.executeUpdate();

				logger.info("Batch complete.");
			}

			transaction.commit();

			logger.info("Done. Inserted a total of {} locations.", locations.size());
		} catch (Exception e) {
			logger.error("Error during location insertion.", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private static List<List<Location>> splitBatches(List<Location> locations, int batchSize) {

		List<List<Location>> result = new ArrayList<>();

		List<Location> curList = new ArrayList<>();
		result.add(curList);

		for (Location location : locations) {
			if (curList.size() == batchSize) {
				curList = new ArrayList<>();
				result.add(curList);
			}

			curList.add(location);
		}

		return result;
	}

}
