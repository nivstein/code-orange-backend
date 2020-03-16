package org.codeorange.backend.db.controllers;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.codeorange.backend.db.queries.TruncateTableQueryBuilder;
import org.codeorange.backend.db.util.HibernateUtil;

public class TableTruncator {

	public static void truncate(String tableName) {
		
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();

			Transaction transaction = session.beginTransaction();
			Query query = TruncateTableQueryBuilder.build(session, tableName);
			
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
