package org.codeorange.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.codeorange.backend.tasks.TaskScheduler;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		TaskScheduler.init();
		SpringApplication.run(BackendApplication.class, args);
	}

}
