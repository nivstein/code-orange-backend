package org.codeorange.backend.db.queries;

import org.hibernate.Query;
import org.hibernate.Session;

public class TruncateTableQueryBuilder {

	public static Query build(Session session, String tableName) {
		return session.createNativeQuery("TRUNCATE TABLE `" + tableName + "`;");
	}

}
