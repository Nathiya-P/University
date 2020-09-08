package com.code.university.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.apache.commons.lang3.StringUtils;

public final class Date {

	private final static String DATE_PATTERN = "dd-MMM-yyyy HH:mm:ss.SSS";
	
	private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
	
	public static LocalDateTime toDate(String date) {
		if(StringUtils.isEmpty(date)) {
			return null;
		}
		
		return LocalDateTime.parse(date, DATE_FORMATTER);
	}
	
	public static String now() {
		LocalDateTime now = LocalDateTime.now();
		return now.format(DATE_FORMATTER);
	}
	
	public static long diiferenceInSconds(LocalDateTime fromDate, LocalDateTime toDate) {
	
		return ChronoUnit.SECONDS.between(fromDate, toDate);
	}
	
	public static long diiferenceInMiliSconds(LocalDateTime fromDate, LocalDateTime toDate) {
		
		return ChronoUnit.MILLIS.between(fromDate, toDate);
	}
	
	public static long diiferenceInNanoSconds(LocalDateTime fromDate, LocalDateTime toDate) {
		
		return ChronoUnit.NANOS.between(fromDate, toDate);
	}
}
