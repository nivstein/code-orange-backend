package org.codeorange.backend.tasks;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.codeorange.backend.data.Country;
import org.codeorange.backend.data.Event;
import org.codeorange.backend.data.Location;
import org.codeorange.backend.data.PatientStatus;
import org.codeorange.backend.datasource.ilmoh.IsraelMOHCovid19DataLoader;
import org.codeorange.backend.db.controllers.LocationsInserter;
import org.codeorange.backend.db.controllers.TableTruncator;
import org.codeorange.backend.db.util.DbUtil;

public class ImportIsraelMOHCovid19DataTask implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(ImportIsraelMOHCovid19DataTask.class);

	@Override
	public void run() {
		logger.info("Running Israel MOH COVID-19 data import task...");

		try {
			internalRun();

			logger.info("Import task complete.");
		} catch (Exception e) {
			logger.error("Error during import task.", e);
		}
	}

	private void internalRun() {
		List<Location> locations = IsraelMOHCovid19DataLoader.create().load();

		if ((locations == null) ||
			(locations.isEmpty())) {

			logger.warn("No locations received from data loader; aborting.");
			return;
		}

		String tableName = DbUtil.TABLE_NAME_IMPORTED_LOCATIONS_IL_MOH;

		logger.info("Received {} locations from data loader; about to insert into table {}.",
			locations.size(), tableName);

		TableTruncator.truncate(tableName);

		LocationsInserter.insert(
			tableName,
			Event.COVID_19.getId(),
			PatientStatus.CARRIER.getId(),
			Country.ISRAEL.getCode(),
			System.currentTimeMillis(),
			locations);
	}

}