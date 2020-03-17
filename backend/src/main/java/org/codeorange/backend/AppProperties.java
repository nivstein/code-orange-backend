package org.codeorange.backend;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.codeorange.backend.util.EnvVarResolver;

public class AppProperties {
	
	private static final Logger logger = LoggerFactory.getLogger(AppProperties.class);

	private static final String FILE_NAME = "application.properties";

	private static volatile AppProperties instance = null;

	public static AppProperties get() {
		if (instance == null) {
			synchronized (AppProperties.class) {
				if (instance == null) {
					instance = create();
				}
			}
		}

		return instance;
	}

	private static AppProperties create() {

		logger.info("About to load app properties from: {}.", FILE_NAME);

		Properties props = new Properties();
		InputStream is = null;

		try {
			is = AppProperties.class.getClassLoader().getResourceAsStream(FILE_NAME);
			props.load(is);

			logger.info("Successfully loaded {} app properties.", props.size());
		} catch (IOException e) {
			logger.error("Error loading app properties.", e);
		} finally {
			IOUtils.closeQuietly(is);
		}

		return new AppProperties(props);
	}

	private final Properties props;

	private AppProperties(Properties props) {
		this.props = props;
	}

	public String get(String key) {
		return get(key, true);
	}

	public String get(String key, boolean resolveEnvVars) {
		String value = props.getProperty(key);

		if ((value == null) ||
			(!resolveEnvVars)) {
			return value;
		}

		return EnvVarResolver.resolve(value);
	}

}
