package org.codeorange.backend.api.events;

import java.util.ArrayList;
import java.util.List;

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

import org.codeorange.backend.data.Country;
import org.codeorange.backend.data.Event;
import org.codeorange.backend.data.Location;
import org.codeorange.backend.data.PatientStatus;
import org.codeorange.backend.db.controllers.LocationsFetcher;
import org.codeorange.backend.db.controllers.LocationsInserter;
import org.codeorange.backend.db.util.DbUtil;

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

		logger.info("About to handle Israel MOH carrier report for: {}; {} locations.", patientCode, locations.size());

		IsraelMOHReportCarrierHandler.handle(patientCode, locations);
		
		logger.info("Done.");

		return ResponseEntity.ok().build();
	}
	//NNNTODO remove up to here

}
