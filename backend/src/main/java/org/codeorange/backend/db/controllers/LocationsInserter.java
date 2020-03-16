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
import org.codeorange.backend.db.queries.InsertLocationsQueryBuilder;
import org.codeorange.backend.db.util.HibernateUtil;

public class LocationsInserter {

	private static final int BATCH_SIZE = 50; //NNNTODO move to config

	public static void insert(String tableName, String eventId, String patientStatus,
		String country, long receivedTimestamp, List<Location> locations) {
		
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			
			Transaction transaction = session.beginTransaction();

			List<List<Location>> locationBatches = splitBatches(locations);

			for (List<Location> locationBatch : locationBatches) {
				Query query = InsertLocationsQueryBuilder.build(session, tableName,
					eventId, patientStatus, country, receivedTimestamp, locationBatch);

				query.executeUpdate();
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private static List<List<Location>> splitBatches(List<Location> locations) {

		List<List<Location>> result = new ArrayList<>();

		List<Location> curList = new ArrayList<>();
		result.add(curList);

		for (Location location : locations) {
			if (curList.size() == BATCH_SIZE) {
				curList = new ArrayList<>();
				result.add(curList);
			}

			curList.add(location);
		}

		return result;
	}

}
