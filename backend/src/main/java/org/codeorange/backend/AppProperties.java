package org.codeorange.backend;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import org.codeorange.backend.util.EnvVarResolver;

public class AppProperties {
	
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
		Properties props = new Properties();
		InputStream is = null;

		try {
			is = AppProperties.class.getClassLoader().getResourceAsStream(FILE_NAME);
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
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
