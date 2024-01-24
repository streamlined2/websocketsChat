package com.streamlined.websockets.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Driver {

	private static final int CLIENT_COUNT = 3;
	private static final long TERMINATION_TIMEOUT = 10L;

	private final ExecutorService executor;

	public Driver() {
		executor = Executors.newFixedThreadPool(CLIENT_COUNT);
	}

	public void run() throws InterruptedException {
		for (int k = 0; k < CLIENT_COUNT; k++) {
			executor.submit(new Client(k));
		}
		executor.awaitTermination(TERMINATION_TIMEOUT, TimeUnit.MINUTES);
	}

	public static void main(String... args) {
		try {
			new Driver().run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
