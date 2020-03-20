package org.codeorange.backend.services.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*package*/ class LogEmailService extends EmailService {

	private static final Logger logger = LoggerFactory.getLogger(LogEmailService.class);

	public static EmailService create() {
		return new LogEmailService();
	}

	private LogEmailService() {

	}

	@Override
	public void sendEmail(String from, String to, String subject, String body) {

		logger.info("+-----------------------------------------------------------------+");
		logger.info("| From:    {}", from);
		logger.info("| To:      {}", to);
		logger.info("| Subject: {}", subject);
		logger.info("+-----------------------------------------------------------------+");

		String[] lines = body.split("\\r?\\n");

		for (String line : lines) {
			logger.info("| {}", line);
		}

		logger.info("+-----------------------------------------------------------------+");
	}

}
