package org.codeorange.backend.services.email;

import java.io.IOException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.codeorange.backend.AppProperties;

/*package*/ class SESEmailService extends EmailService {

	private static final Logger logger = LoggerFactory.getLogger(SESEmailService.class);

	public static EmailService create() {
		AWSCredentials creds = new BasicAWSCredentials(
				AppProperties.get().get("code-orange.aws-access-key"),
				AppProperties.get().get("code-orange.aws-secret-key"));

		AmazonSimpleEmailService client =
				AmazonSimpleEmailServiceClientBuilder.standard()
					.withRegion(Regions.US_EAST_1)
					.withCredentials(new AWSStaticCredentialsProvider(creds))
					.build();

		return new SESEmailService(client);
	}
	
	private final AmazonSimpleEmailService client;

	private SESEmailService(AmazonSimpleEmailService client) {
		this.client = client;
	}

	@Override
	public void sendEmail(String from, String to, String subject, String body) {

		try {
			SendEmailRequest request = new SendEmailRequest()
					.withDestination(new Destination()
						.withToAddresses(to))
					.withMessage(new Message()
						.withBody(new Body()
							.withHtml(new Content()
								.withCharset("UTF-8")
								.withData(body))
							.withText(new Content()
								.withCharset("UTF-8")
								.withData(body)))
						.withSubject(new Content()
							.withCharset("UTF-8")
							.withData(subject)))
					.withSource(from);

			client.sendEmail(request);
		} catch (Exception e) {
			logger.error("Error sending email via SES.", e);
		}
	}

}
