package com.code.university.Exception;

public class StudentAlreadyEnrolledInCourseException extends Exception {

	private static final long serialVersionUID = 1L;

	public StudentAlreadyEnrolledInCourseException() {
		
	}
	
	public StudentAlreadyEnrolledInCourseException(String message) {
		super(message);
	}
}
