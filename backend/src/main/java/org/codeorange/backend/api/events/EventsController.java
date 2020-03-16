package org.codeorange.backend.api.events;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.codeorange.backend.api.data.Location;
import org.codeorange.backend.db.fetchers.LocationsFetcher;

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
	public ResponseEntity<String> updateLocations(@PathVariable(value = "eventId") String eventId) {
		//NNNTODO: Implement
		return ResponseEntity.ok().build();
	}

}
