package org.codeorange.backend.tasks;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskScheduler {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskScheduler.class);

	private static ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

	public static void init() {
		 ses.scheduleAtFixedRate(new ImportIsraelMOHCovid19DataTask(), 1, 60, TimeUnit.MINUTES);

		 logger.info("Scheduled tasks initialized.");
	}

}
