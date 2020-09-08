package com.code.university.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Consumer;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Course {

	private String courseName;
	private int capacity;
	private DayOfWeek dayNumber;
	private LocalDate startDate;
	private LocalDate endDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private String location;

	public String getCourseName() {
		return courseName;
	}

	public Course setCourseName(String courseName) {
		this.courseName = courseName;
		return this;
	}

	public int getCapacity() {
		return capacity;
	}

	public Course setCapacity(int capacity) {
		this.capacity = capacity;
		return this;
	}

	public DayOfWeek getDayNumber() {
		return dayNumber;
	}

	public Course setDayNumber(DayOfWeek dayNumber) {
		this.dayNumber = dayNumber;
		return this;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public Course setStartDate(LocalDate startDate) {
		this.startDate = startDate;
		return this;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public Course setEndDate(LocalDate endDate) {
		this.endDate = endDate;
		return this;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public Course setStartTime(LocalTime startTime) {
		this.startTime = startTime;
		return this;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public Course setEndTime(LocalTime endTime) {
		this.endTime = endTime;
		return this;
	}

	public String getLocation() {
		return location;
	}

	public Course setLocation(String location) {
		this.location = location;
		return this;
	}

	public static Course build(Consumer<Course> course) {
		Course cours = new Course();
		course.accept(cours);
		return cours;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
