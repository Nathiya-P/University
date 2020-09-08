package com.code.university.util;

public class Stopwatch {

	private long startTime = 0;
	private long stopTime = 0;
	private final long nsPerTick = 100;

	public void start() {
		this.startTime = System.nanoTime();
	}

	public void stop() {
		this.stopTime = System.nanoTime();
	}

	public long getElapsedTicks() {
		return (stopTime - startTime) / nsPerTick;
	}
}
