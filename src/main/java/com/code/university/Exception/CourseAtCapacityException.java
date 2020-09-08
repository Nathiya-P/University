package com.code.university.Exception;

public class CourseAtCapacityException extends Exception {

	private static final long serialVersionUID = 1L;

	public CourseAtCapacityException() {
		
	}
	
	public CourseAtCapacityException(String message) {
		super(message);
	}
}
