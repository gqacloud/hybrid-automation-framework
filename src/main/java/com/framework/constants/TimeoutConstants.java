package com.framework.constants;

import java.time.Duration;

/**
 * Centralized timeout constants for the framework. Pure constants only — no
 * config loading logic here.
 */
public final class TimeoutConstants {

	private TimeoutConstants() {
		// prevent instantiation
	}

	// =========================
	// DEFAULT TIMEOUTS
	// =========================
	public static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(30);
	public static final Duration EXPLICIT_WAIT = Duration.ofSeconds(15);
	public static final Duration IMPLICIT_WAIT = Duration.ofSeconds(5);
	public static final Duration SCRIPT_TIMEOUT = Duration.ofSeconds(30);

	// =========================
	// WAIT CONFIG
	// =========================
	public static final Duration POLLING_INTERVAL = Duration.ofMillis(500);

	// =========================
	// RETRY CONFIG
	// =========================
	public static final int NAVIGATION_RETRY_COUNT = 2;
}
