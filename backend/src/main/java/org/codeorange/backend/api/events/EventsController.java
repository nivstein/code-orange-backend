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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.codeorange.backend.AppProperties;
import org.codeorange.backend.data.Country;
import org.codeorange.backend.data.Event;
import org.codeorange.backend.data.Location;
import org.codeorange.backend.data.PatientStatus;
import org.codeorange.backend.db.controllers.LocationsFetcher;
import org.codeorange.backend.db.controllers.LocationsInserter;
import org.codeorange.backend.db.util.DbUtil;
import org.codeorange.backend.services.email.EmailService;
import org.codeorange.backend.util.DateUtil;

@RestController
public class EventsController {

	private static final Logger logger = LoggerFactory.getLogger(EventsController.class);

	@GetMapping("/v1/events/{eventId}/locations")
	public ResponseEntity<GetLocationsResponse> getLocations(
			@PathVariable(value = "eventId")						  String eventId,
			@RequestParam(value = "min_entry_time", required = false) String minEntryTime,
			@RequestParam(value = "patient_status", required = false) String patientStatus,
			@RequestParam(value = "country",		required = false) String country) {

		logger.info("Received getLocations request: [{}; {}; {}; {}].", eventId, minEntryTime, patientStatus, country);

		List<Location> locations = LocationsFetcher.fetch(eventId, minEntryTime, patientStatus, country);

		if (locations == null) {

			logger.warn("No locations retrieved due to bad request.");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		logger.info("Returning {} locations.", locations.size());

		return ResponseEntity.ok(new GetLocationsResponse(locations));
	}

	@PostMapping("/v1/events/{eventId}/locations")
	public ResponseEntity<String> updateLocations(
			@RequestBody PostLocationsRequest request,
			@PathVariable(value = "eventId") String eventId) {

		logger.info("Received updateLocations request: [{}; {}; {}].", eventId, request.getCountry(), request.getPatientStatus());

		//NNNTODO perform generic param validation
		if ((!Event.COVID_19.getId().equalsIgnoreCase(eventId)) ||
			(!Country.ISRAEL.getCode().equalsIgnoreCase(request.getCountry())) ||
			(!PatientStatus.CARRIER.getId().equalsIgnoreCase(request.getPatientStatus()))) {

			logger.warn("Bad input ({}; {}; {})!", eventId, request.getCountry(), request.getPatientStatus());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		List<Location> locations = request.getLocations();

		if ((locations != null) &&
			(!locations.isEmpty())) {

			logger.info("About to insert {} locations.", locations.size());

			LocationsInserter.insert(
				DbUtil.TABLE_NAME_RECORDED_LOCATIONS,
				eventId,
				request.getPatientStatus(),
				request.getCountry(),
				System.currentTimeMillis(),
				locations);
		}

		logger.info("Done inserting new locations.");

		return ResponseEntity.ok().build();
	}

	// NNNTODO: Remove this after proper channel w/ IL MOH is established
	//
	// Temp API for forwarding confirmed carrier locations to IL MOH
	//
	@PostMapping("/v1/events/{eventId}/external/il-moh-report")
	public ResponseEntity<String> reportCarrier(
			@RequestBody IsraelMOHReportCarrierRequest request,
			@PathVariable(value = "eventId") String eventId) {

		logger.info("Received Israel MOH reportCarrier request: [{}; {}].", eventId, request.getPatientCode());

		if (!Event.COVID_19.getId().equalsIgnoreCase(eventId)) {

			logger.warn("Bad event ID ({}); returning bad request.", eventId);

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		int patientCode = request.getPatientCode();
		List<Location> locations = request.getLocations();

		if (locations == null) {

			logger.warn("No locations received; returning bad request.");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		String fromAddress = AppProperties.get().get("code-orange.email.from-address");
		String toAddress = AppProperties.get().get("code-orange.il-moh-report.to-address");

		String subjectFormat = "צבע כתום: [קוד חולה: %d] חולה COVID-19 מאומת";
		String subject = String.format(subjectFormat, patientCode);

		String body = buildIsraelMOHReportEmailBody(patientCode, locations);

		logger.info("About to send email: {} -> {}; \"{}\"; body length: {}.",
			fromAddress, toAddress, subject, body.length());

		EmailService.get().sendEmail(fromAddress, toAddress, subject, body);

		logger.info("Done sending email.");
		
		return ResponseEntity.ok().build();
	}

	private String buildIsraelMOHReportEmailBody(int patientCode, List<Location> locations) {

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
	//NNNTODO remove up to here

}
