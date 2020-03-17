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

import org.codeorange.backend.AppProperties;

/*package*/ class SESEmailService extends EmailService {

	@Override
	public void sendEmail(String from, String to, String subject, String body) {

		try {
			AWSCredentials creds = new BasicAWSCredentials(
					AppProperties.get().get("code-orange.aws-access-key"),
					AppProperties.get().get("code-orange.aws-secret-key"));

			AmazonSimpleEmailService client =
					AmazonSimpleEmailServiceClientBuilder.standard()
						.withRegion(Regions.US_EAST_1)
						.withCredentials(new AWSStaticCredentialsProvider(creds))
						.build();

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
			e.printStackTrace();
		}
	}

}
