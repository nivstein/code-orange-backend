package org.codeorange.backend.db.util;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

public class HibernateUtil {

	private static volatile SessionFactory sessionFactory = null;

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			synchronized (HibernateUtil.class) {
				if (sessionFactory == null) {
					try {
						sessionFactory = createSessionFactory();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return sessionFactory;
	}

	private static SessionFactory createSessionFactory() {
		StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

		Map<String, Object> settings = new HashMap<>(); //NNNTODO: KILL THIS WHOLE THING
		settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
		settings.put(Environment.URL, "jdbc:mysql://localhost:3306/codeorange");
		settings.put(Environment.USER, "root");
		settings.put(Environment.PASS, "root");

		registryBuilder.applySettings(settings);

		StandardServiceRegistry registry = registryBuilder.build();

		MetadataSources sources = new MetadataSources(registry);
		Metadata metadata = sources.getMetadataBuilder().build();

		return metadata.getSessionFactoryBuilder().build();
	}
}
