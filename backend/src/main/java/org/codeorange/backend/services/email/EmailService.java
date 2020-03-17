package org.codeorange.backend.services.email;

public abstract class EmailService {

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
		return new SESEmailService();
	}

	public abstract void sendEmail(String from, String to, String subject, String body);

}
