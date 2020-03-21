package org.codeorange.backend.db.controllers;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.codeorange.backend.db.queries.TruncateTableQueryBuilder;
import org.codeorange.backend.db.util.HibernateUtil;

public class TableTruncator {

	private static final Logger logger = LoggerFactory.getLogger(TableTruncator.class);

	public static void truncate(String tableName) {
		
		logger.info("About to truncate table {}...", tableName);

		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();

			Transaction transaction = session.beginTransaction();
			Query<?> query = TruncateTableQueryBuilder.build(session, tableName);
			
			query.executeUpdate();
			
			transaction.commit();

			logger.info("Truncation done.");
		} catch (Exception e) {
			logger.error("Error truncating table.", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}
