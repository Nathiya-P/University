package com.code.university.util;

import static com.code.university.util.Constants.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.math3.util.Precision;

public final class Utility {

	public static String getFirstNameString() {
		return RandomStringUtils.randomAlphabetic(10);
	}

	public static String getLastNameString() {
		return RandomStringUtils.randomAlphabetic(7);
	}

	public static String getCourseString() {
		return RandomStringUtils.randomAlphabetic(5);
	}

	public static Double getGPADouble(double min, double max) {
		Double number = min + new Random().nextDouble() * (max - min);
		return Precision.round(number, 1);
	}

	public static String getLocationString() {
		return RandomStringUtils.randomAlphabetic(10);
	}

	public static int createRandomIntBetween(int start, int end) {
		return start + (int) Math.round(Math.random() * (end - start));
	}

	public static LocalDate getRandomDate(int startYear, int endYear) {
		int day = createRandomIntBetween(1, 28);
		int month = createRandomIntBetween(1, 12);
		int year = createRandomIntBetween(startYear, endYear);
		return LocalDate.of(year, month, day);
	}

	public static LocalTime getRandomTime() {
		Random generator = new Random(System.nanoTime());
		LocalTime time = LocalTime.MIN.plusSeconds(generator.nextLong());
		return time;
	}

	public static List<Long> skipOutliers(List<Long> input) {
		List<Long> output = new ArrayList<Long>();
		List<Long> data1 = new ArrayList<Long>();
		List<Long> data2 = new ArrayList<Long>();
		List<Long> sorted = input.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
		if (sorted.size() % 2 == 0) {
			data1 = sorted.subList(0, sorted.size() / 2);
			data2 = sorted.subList(sorted.size() / 2, sorted.size());
		} else {
			data1 = sorted.subList(0, sorted.size() / 2);
			data2 = sorted.subList(sorted.size() / 2 + 1, sorted.size());
		}
		double q1 = getMedian(data1);
		double q3 = getMedian(data2);
		double iqr = q3 - q1;
		double lowerFence = q1 - 1.5 * iqr;
		double upperFence = q3 + 1.5 * iqr;
		for (int i = 0; i < sorted.size(); i++) {
			if (sorted.get(i) < lowerFence || sorted.get(i) > upperFence)
				output.add(sorted.get(i));
		}

		sorted.removeAll(output);
		return sorted;
	}

	private static double getMedian(List<Long> data) {
		if (data.size() % 2 == 0)
			return (data.get(data.size() / 2) + data.get(data.size() / 2 - 1)) / 2;
		else
			return data.get(data.size() / 2);
	}
}
