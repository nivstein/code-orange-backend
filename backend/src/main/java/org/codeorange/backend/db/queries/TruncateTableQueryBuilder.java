package org.codeorange.backend.db.queries;

import org.hibernate.Session;
import org.hibernate.query.Query;

public class TruncateTableQueryBuilder {

	public static Query<?> build(Session session, String tableName) {
		return session.createNativeQuery("TRUNCATE TABLE `" + tableName + "`;");
	}

}
