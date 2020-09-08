package com.code.university.model;

import java.util.function.Consumer;

public class CourseEnrollment {

	private Student student;
	private Course course;
	
	public Student getStudent() {
		return student;
	}
	
	public CourseEnrollment setStudent(Student student) {
		this.student = student;
		return this;
	}
	
	public Course getCourse() {
		return course;
	}
	
	public CourseEnrollment setCourse(Course course) {
		this.course = course;
		return this;
	}
	
	public static CourseEnrollment build(Consumer<CourseEnrollment> courseEnroll) {
		CourseEnrollment rollment = new CourseEnrollment();
		courseEnroll.accept(rollment);
		return rollment;
	}
}
