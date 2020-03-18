package org.codeorange.backend.api.events;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.codeorange.backend.AppProperties;
import org.codeorange.backend.data.Location;
import org.codeorange.backend.services.email.EmailService;
import org.codeorange.backend.util.DateUtil;

//NNNTODO: Kill this class once MOH create their own funnel for this.
//
public class IsraelMOHReportCarrierHandler {

	private static final Logger logger = LoggerFactory.getLogger(IsraelMOHReportCarrierHandler.class);
	
	public static void handle(int patientCode, List<Location> locations) {
		String fromAddress = AppProperties.get().get("code-orange.email.from-address");
		String toAddress = AppProperties.get().get("code-orange.il-moh-report.to-address");

		String subjectFormat = "צבע כתום: [קוד חולה: %d] חולה COVID-19 מאומת";
		String subject = String.format(subjectFormat, patientCode);

		String body = buildEmailBody(patientCode, locations);

		logger.info("About to send email: {} -> {}; \"{}\"; body length: {}.",
			fromAddress, toAddress, subject, body.length());

		EmailService.get().sendEmail(fromAddress, toAddress, subject, body);
	}

	private static String buildEmailBody(int patientCode, List<Location> locations) {

		StringBuilder sb = new StringBuilder();

		sb.append("<div dir=\"rtl\">");
		sb.append("<h1>פרטי חולה מאומת מאפליקציית צבע כתום</h1>");
		sb.append("<h2>קוד חולה: <b>").append(patientCode).append("</b></h2>");

		sb.append("במהלך 14 הימים האחרונים, החולה ביקר במיקומים הבאים:<br><br>");

		List<Location> sortedLocations = new ArrayList<>(locations);
		sortedLocations.sort((l1, l2) -> l1.getStartTime().compareTo(l2.getStartTime()));

		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Israel"));

        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        timeFormat.setTimeZone(TimeZone.getTimeZone("Israel"));

		for (int i = 0; i < sortedLocations.size(); i++) {
			Location location = sortedLocations.get(i);

			Date startTime = location.getStartTime();
			Date endTime = location.getEndTime();

			double lat = location.getLat();
			double lon = location.getLon();

			sb.append("[").append(i + 1).append("] ")
			  .append("<b>").append(dateFormat.format(startTime)).append("</b> ")
			  .append(timeFormat.format(startTime)).append("-").append(timeFormat.format(endTime))
			  .append(": <b>").append(lat).append(", ").append(lon).append("</b>")
			  .append(" (<a href=\"https://maps.google.com/?q=").append(lat).append(',').append(lon).append("\">הצג על מפה</a>)")
			  .append("<br>");
		}

		sb.append("<br><b><i>נאסף ונשלח על-ידי אפליקציית צבע כתום.</i></b>");
		sb.append("</div>");

		return sb.toString();
	}

}
