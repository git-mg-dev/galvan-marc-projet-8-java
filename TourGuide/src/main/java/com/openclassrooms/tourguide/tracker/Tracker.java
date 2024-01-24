package com.openclassrooms.tourguide.tracker;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openclassrooms.tourguide.service.TourGuideService;
import com.openclassrooms.tourguide.model.User;

public class Tracker extends Thread {
	private final Logger logger = LoggerFactory.getLogger(Tracker.class);
	private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final TourGuideService tourGuideService;
	private final HashMap<UUID, Boolean> trackingMap;
	private boolean stop = false;

	public Tracker(TourGuideService tourGuideService) {
		this.tourGuideService = tourGuideService;
		this.trackingMap = new HashMap<>();

		executorService.submit(this);
	}

	/**
	 * Assures to shut down the Tracker thread
	 */
	public void stopTracking() {
		stop = true;
		executorService.shutdownNow();
	}

	@Override
	public void run() {
		StopWatch stopWatch = new StopWatch();
		while (true) {
			if (Thread.currentThread().isInterrupted() || stop) {
				logger.debug("Tracker stopping");
				break;
			}

			List<User> users = tourGuideService.getAllUsers();
			logger.debug("Begin Tracker. Tracking " + users.size() + " users.");
			stopWatch.start();

			// Tracking users location and calculating rewards
			users.forEach(tourGuideService::trackUserLocation);

			// Wait for all rewards to be calculated
			boolean finished = false;
			while(!finished && trackingMap.size() < users.size()) {
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					break;
				}

				if(!trackingMap.containsValue(false)) {
					finished = true;
				}
			}
			trackingMap.clear();

			stopWatch.stop();
			logger.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
			stopWatch.reset();
			try {
				logger.debug("Tracker sleeping");
				TimeUnit.SECONDS.sleep(trackingPollingInterval);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	public void endUserTracking(UUID userId) {
		trackingMap.put(userId, true);
	}
}
