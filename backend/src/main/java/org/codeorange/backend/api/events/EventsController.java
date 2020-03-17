package org.codeorange.backend.api.events;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

	@GetMapping("/v1/events/{eventId}/locations")
	public ResponseEntity<GetLocationsResponse> getLocations(
			@PathVariable(value = "eventId")						  String eventId,
			@RequestParam(value = "min_entry_time", required = false) String minEntryTime,
			@RequestParam(value = "patient_status", required = false) String patientStatus,
			@RequestParam(value = "country",		required = false) String country) {

		System.out.println("Received GetLocations request: [" +
			eventId + ", " + minEntryTime + ", " + patientStatus + ", " + country + "]");

		List<Location> locations = LocationsFetcher.fetch(eventId, minEntryTime, patientStatus, country);

		if (locations == null) {
			System.out.println("No locations retrieved; bad request.");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		System.out.println("Returning " + locations.size() + " locations.");

		return ResponseEntity.ok(new GetLocationsResponse(locations));
	}

	@PostMapping("/v1/events/{eventId}/locations")
	public ResponseEntity<String> updateLocations(
			@RequestBody PostLocationsRequest request,
			@PathVariable(value = "eventId") String eventId) {

		System.out.println("Received UpdateLocations request: [" +
			eventId + ", " + request.getCountry() + ", " + request.getPatientStatus() + "]");

		//NNNTODO perform generic param validation
		if ((!Event.COVID_19.getId().equalsIgnoreCase(eventId)) ||
			(!Country.ISRAEL.getCode().equalsIgnoreCase(request.getCountry())) ||
			(!PatientStatus.CARRIER.getId().equalsIgnoreCase(request.getPatientStatus()))) {
			System.out.println("Bad input; bad request.");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		List<Location> locations = request.getLocations();

		if ((locations != null) &&
			(!locations.isEmpty())) {
			System.out.println("About to insert " + locations.size() + " locations.");

			LocationsInserter.insert(
				DbUtil.TABLE_NAME_RECORDED_LOCATIONS,
				eventId,
				request.getPatientStatus(),
				request.getCountry(),
				System.currentTimeMillis(),
				locations);
		}

		System.out.println("Done.");

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

		System.out.println("Received IsraelMOHReportCarrier request: [" + eventId + ", " + request.getPatientCode() + "]");

		if (!Event.COVID_19.getId().equalsIgnoreCase(eventId)) {
			System.out.println("Bad event ID; bad request.");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		int patientCode = request.getPatientCode();
		List<Location> locations = request.getLocations();

		if (locations == null) {
			System.out.println("No locations received; bad request.");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		String fromAddress = AppProperties.get().get("code-orange.email.from-address");
		String toAddress = AppProperties.get().get("code-orange.il-moh-report.to-address");

		String subjectFormat = AppProperties.get().get("code-orange.il-moh-report.subject-format");
		String subject = String.format(subjectFormat, patientCode);

		String body = buildIsraelMOHReportEmailBody(patientCode, locations);

		System.out.println("About to send email: [" + fromAddress + ", " + toAddress + ", " +
			subject + ", " + body + "]");

		EmailService.get().sendEmail(fromAddress, toAddress, subject, body);

		System.out.println("Done.");
		
		return ResponseEntity.ok().build();
	}

	private String buildIsraelMOHReportEmailBody(int patientCode, List<Location> locations) {

		StringBuilder sb = new StringBuilder();

		sb.append("<h1>Forwarded details for COVID-19 confirmed patient</h1>");
		sb.append("<h2>Patient code: <b>").append(patientCode).append("</b></h2>");

		sb.append("Over the past 14 days, the patient has visited the following locations:<br><br>");

		List<Location> sortedLocations = new ArrayList<>(locations);
		sortedLocations.sort((l1, l2) -> l1.getStartTime().compareTo(l2.getStartTime()));

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
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
			  .append(" (<a href=\"https://maps.google.com/?q=").append(lat).append(',').append(lon).append("\">Show on map</a>)")
			  .append("<br>");
		}

		sb.append("<br><b><i>Forwarded by Code Orange.</i></b>");

		return sb.toString();
	}
	//NNNTODO remove up to here

}
