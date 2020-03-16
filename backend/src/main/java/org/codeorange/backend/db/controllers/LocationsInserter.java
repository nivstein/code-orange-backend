package org.codeorange.backend.db.controllers;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.codeorange.backend.api.data.Location;
import org.codeorange.backend.db.queries.InsertLocationsQueryBuilder;
import org.codeorange.backend.db.util.HibernateUtil;

public class LocationsInserter {

	public static void insert(String tableName, String eventId, String patientStatus,
		String country, long receivedTimestamp, List<Location> locations) {
		
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();

			Transaction transaction = session.beginTransaction();
			Query query = InsertLocationsQueryBuilder.build(session, tableName,
				eventId, patientStatus, country, receivedTimestamp, locations);

			query.executeUpdate();

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}
