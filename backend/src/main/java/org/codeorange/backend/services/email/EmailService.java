package org.codeorange.backend.services.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.codeorange.backend.AppProperties;

public abstract class EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	
	private static volatile EmailService instance = null;

	public static EmailService get() {
		if (instance == null) {
			synchronized (EmailService.class) {
				if (instance == null) {
					instance = create();
				}
			}
		}

		return instance;
	}

	private static EmailService create() {
		return Type.valueOf(AppProperties.get().get("code-orange.email.service")).create();
	}

	public abstract void sendEmail(String from, String to, String subject, String body);

	private static enum Type {

		SES(SESEmailService.class),
		LOG(LogEmailService.class);

		private final Class<? extends EmailService> clazz;

		Type(Class<? extends EmailService> clazz) {
			this.clazz = clazz;
		}

		public EmailService create() {
			try {
				return (EmailService)clazz.getMethod("create").invoke(null);
			} catch (Exception e) {
				logger.error("Error instantiating Email service {}.", this, e);
				return null;
			}
		}
	}

}
