package org.codeorange.backend.tasks;

import java.util.List;

import org.codeorange.backend.api.data.Location;
import org.codeorange.backend.datasource.ilmoh.IsraelMOHCovid19DataLoader;

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

		for (Location location : locations) {
			System.out.println("--> Loaded location: " + location);
		}
	}

}