package org.codeorange.backend.tasks;

import org.codeorange.backend.datasource.ilmoh.IsraelMOHCovid19DataLoader;

public class ImportIsraelMOHCovid19DataTask implements Runnable {
	
	@Override
	public void run() {
		try {
			IsraelMOHCovid19DataLoader.create().load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}