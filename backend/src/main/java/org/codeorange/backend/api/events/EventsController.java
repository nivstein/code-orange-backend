package org.codeorange.backend.api.events;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.codeorange.backend.api.data.Location;
import org.codeorange.backend.db.queries.GetLocationsQueryBuilder;
import org.codeorange.backend.db.util.HibernateUtil;

@RestController
public class EventsController {

	@GetMapping("/v1/events/{eventId}/locations")
	public ResponseEntity<GetLocationsResponse> getLocations(
			@PathVariable(value = "eventId")						  String eventId,
			@RequestParam(value = "min_entry_time", required = false) String minEntryTime,
			@RequestParam(value = "patient_status", required = false) String patientStatus,
			@RequestParam(value = "country",		required = false) String country) {

		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();

			Transaction transaction = session.beginTransaction();
			Query query = GetLocationsQueryBuilder.build(session, eventId, minEntryTime, patientStatus, country);

			List<Object[]> rawLocations = query.list();
			List<Location> locations = new ArrayList<>();

			for (Object[] rawLocation : rawLocations) {
				locations.add(new Location(
					new Date(((BigInteger)rawLocation[0]).longValue()),
					new Date(((BigInteger)rawLocation[1]).longValue()),
					(Double)rawLocation[2],
					(Double)rawLocation[3],
					(Double)rawLocation[4]));
			}

			transaction.commit();

			return ResponseEntity.ok(new GetLocationsResponse(locations));
		} catch (ParseException e) {
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@PostMapping("/v1/events/{eventId}/locations")
	public ResponseEntity<String> updateLocations(@PathVariable(value = "eventId") String eventId) {
		//NNNTODO: Implement
		return ResponseEntity.ok().build();
	}

}
