package com.code.university.model;

import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Student {

	private String firstName;
	private String lastName;
	private Double gpa;

	public String getFirstName() {
		return firstName;
	}

	public Student setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public Student setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public Double getGpa() {
		return gpa;
	}

	public Student setGpa(Double gpa) {
		this.gpa = gpa;
		return this;
	}

	public static Student build(Consumer<Student> stud) {
		Student student = new Student();
		stud.accept(student);
		return student;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
