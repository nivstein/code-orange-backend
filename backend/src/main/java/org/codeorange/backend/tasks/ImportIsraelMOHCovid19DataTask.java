package org.codeorange.backend.tasks;

import java.util.List;

import org.codeorange.backend.data.Country;
import org.codeorange.backend.data.Event;
import org.codeorange.backend.data.Location;
import org.codeorange.backend.data.PatientStatus;
import org.codeorange.backend.datasource.ilmoh.IsraelMOHCovid19DataLoader;
import org.codeorange.backend.db.controllers.LocationsInserter;
import org.codeorange.backend.db.controllers.TableTruncator;
import org.codeorange.backend.db.util.DbUtil;

public class ImportIsraelMOHCovid19DataTask implements Runnable {
	
	@Override
	public void run() {
		try {
			internalRun();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void internalRun() {
		List<Location> locations = IsraelMOHCovid19DataLoader.create().load();

		if (locations == null) {
			return;
		}

		String tableName = DbUtil.TABLE_NAME_IMPORTED_LOCATIONS_IL_MOH;

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