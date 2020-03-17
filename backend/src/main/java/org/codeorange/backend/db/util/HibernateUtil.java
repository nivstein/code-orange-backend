package org.codeorange.backend.db.util;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.codeorange.backend.AppProperties;

public class HibernateUtil {

	private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

	private static volatile SessionFactory sessionFactory = null;

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			synchronized (HibernateUtil.class) {
				if (sessionFactory == null) {
					try {
						sessionFactory = createSessionFactory();
					} catch (Exception e) {
						logger.error("Error creating Hibernate session factory.", e);
					}
				}
			}
		}

		return sessionFactory;
	}

	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

	private static SessionFactory createSessionFactory() {
		StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

		Map<String, Object> settings = new HashMap<>();

		settings.put(Environment.DRIVER, JDBC_DRIVER);
		settings.put(Environment.URL,	 AppProperties.get().get("spring.datasource.url"));
		settings.put(Environment.USER,	 AppProperties.get().get("spring.datasource.username"));
		settings.put(Environment.PASS,	 AppProperties.get().get("spring.datasource.password"));

		registryBuilder.applySettings(settings);

		StandardServiceRegistry registry = registryBuilder.build();

		MetadataSources sources = new MetadataSources(registry);
		Metadata metadata = sources.getMetadataBuilder().build();

		return metadata.getSessionFactoryBuilder().build();
	}

}
