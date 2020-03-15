package org.codeorange.backend.tasks;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskScheduler {
	
	private static ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

	public static void init() {
		 ses.scheduleAtFixedRate(new ImportIsraelMOHCovid19DataTask(), 1, 60, TimeUnit.MINUTES);
	}

}