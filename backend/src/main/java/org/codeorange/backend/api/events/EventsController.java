package org.codeorange.backend.api.events;

import java.util.List;

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

	@GetMapping("/v1/events/{eventId}/locations")
	public ResponseEntity<GetLocationsResponse> getLocations(
			@PathVariable(value = "eventId")						  String eventId,
			@RequestParam(value = "min_entry_time", required = false) String minEntryTime,
			@RequestParam(value = "patient_status", required = false) String patientStatus,
			@RequestParam(value = "country",		required = false) String country) {

		List<Location> locations = LocationsFetcher.fetch(eventId, minEntryTime, patientStatus, country);

		if (locations == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		return ResponseEntity.ok(new GetLocationsResponse(locations));
	}

	@PostMapping("/v1/events/{eventId}/locations")
	public ResponseEntity<String> updateLocations(
			@RequestBody PostLocationsRequest request,
			@PathVariable(value = "eventId") String eventId) {

		//NNNTODO perform generic param validation
		if ((!Event.COVID_19.getId().equalsIgnoreCase(eventId)) ||
			(!Country.ISRAEL.getCode().equalsIgnoreCase(request.getCountry())) ||
			(!PatientStatus.CARRIER.getId().equalsIgnoreCase(request.getPatientStatus()))) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		List<Location> locations = request.getLocations();

		if (locations != null) {
			LocationsInserter.insert(
				DbUtil.TABLE_NAME_RECORDED_LOCATIONS,
				eventId,
				request.getPatientStatus(),
				request.getCountry(),
				System.currentTimeMillis(),
				locations);
		}

		return ResponseEntity.ok().build();
	}

}
