package com.code.university.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import com.code.university.Exception.CourseAtCapacityException;
import com.code.university.Exception.StudentAlreadyEnrolledInCourseException;
import com.code.university.model.Course;
import com.code.university.model.CourseEnrollment;
import com.code.university.model.Student;
import com.code.university.util.Utility;

public class RegisterService {

	private static Logger LOGGER = Logger.getLogger(RegisterService.class);
	private List<Student> students = new ArrayList<Student>();
	private List<Course> courses = new ArrayList<Course>();
	private List<CourseEnrollment> courseEnrollments = new ArrayList<CourseEnrollment>();
	private List<Student> sortedStudents = new ArrayList<Student>();

	public void registerStudent(Student student) {
		students.add(student);

		LOGGER.info("Registered " + students.size() + " students");
	}

	public void registerStudents(List<Student> students) {
		for (Student student : students) {
			registerStudent(student);
		}

		sortedStudents = students.stream().sorted(Comparator.comparingDouble(Student::getGpa))
				.collect(Collectors.toList());

		LOGGER.info("Registered " + students.size() + " students");
	}

	public void dropStudentFromCourse(Student student, Course course) {

		List<CourseEnrollment> enrolls = courseEnrollments.stream()
				.filter(c -> c.getCourse().getCourseName().equalsIgnoreCase(course.getCourseName())
						&& c.getCourse().getCapacity() == course.getCapacity()
						&& c.getCourse().getDayNumber() == (course.getDayNumber())
						&& c.getCourse().getStartDate().equals(course.getStartDate())
						&& c.getCourse().getEndDate().equals(course.getEndDate())
						&& c.getCourse().getEndDate().equals(course.getEndDate())
						&& c.getCourse().getEndTime().equals(course.getEndTime())
						&& c.getCourse().getLocation().equalsIgnoreCase(course.getLocation())
						&& c.getStudent().getFirstName().equalsIgnoreCase(student.getFirstName())
						&& c.getStudent().getLastName().equalsIgnoreCase(student.getLastName()))
				.collect(Collectors.toList());

		courseEnrollments.removeAll(enrolls);
	}

	public int calculateNumberOfStudentsWithMinimumGpaOf(double minGPA) {

		// throw new UnsupportedOperationException("Waiting to be implemented.");

		// Using Binary Search
		/*
		 * 
		 * 
		 * int low = 0; int high = sortedStudents.size(); int index =
		 * sortedStudents.size();
		 * 
		 * 
		 * while (low <= high) { int mid = low + (high - low) / 2; double value =
		 * sortedStudents.get(mid).getGpa().doubleValue(); if (value >= minGPA) { index
		 * = mid; high = mid - 1; } else { low = mid + 1; } }
		 * 
		 * int number = sortedStudents.size() - index; LOGGER.info( number +
		 * " number of students got minimum GPA of " + minGPA);
		 */

		// Using Java 8 Stream
		List<Student> studentsMinGPA = sortedStudents.stream().filter(stud -> stud.getGpa().doubleValue() >= minGPA)
				.collect(Collectors.toList());

		LOGGER.info(studentsMinGPA.size() + " number of students got minimum GPA of " + minGPA);

		return studentsMinGPA.size();
	}

	public List<Student> getRegisteredStudents() {
		return students;
	}

	public List<Student> getStudentsEnrolledInCourse(Course course) {

		return courseEnrollments.stream()
				.filter(c -> c.getCourse().getCourseName().equalsIgnoreCase(course.getCourseName())
						&& c.getCourse().getCapacity() == course.getCapacity()
						&& c.getCourse().getDayNumber() == (course.getDayNumber())
						&& c.getCourse().getStartDate().equals(course.getStartDate())
						&& c.getCourse().getEndDate().equals(course.getEndDate())
						&& c.getCourse().getEndTime().equals(course.getEndTime())
						&& c.getCourse().getLocation().equalsIgnoreCase(course.getLocation()))
				.map(ce -> ce.getStudent()).collect(Collectors.toList());

	}

	public void enrollStudentInCourse(Student student, Course course)
			throws StudentAlreadyEnrolledInCourseException, CourseAtCapacityException {
		synchronized (courseEnrollments) {
			List<CourseEnrollment> ce = courseEnrollments.stream()
					.filter(c -> c.getCourse().getCourseName().equalsIgnoreCase(course.getCourseName())) 
					.collect(Collectors.toList());
			if (ce.size() == course.getCapacity()) {
				LOGGER.info("COurse " + course.getCourseName() + " is full. Student " + student.getFirstName()
						+ " cannot register in the course");
				throw new CourseAtCapacityException("Course is full");
			}

			CourseEnrollment enroll = courseEnrollments.stream()
					.filter(c -> c.getCourse().getCourseName().equalsIgnoreCase(course.getCourseName())
							&& c.getCourse().getCapacity() == course.getCapacity()
							&& c.getCourse().getDayNumber() == (course.getDayNumber())
							&& c.getCourse().getStartDate().equals(course.getStartDate())
							&& c.getCourse().getEndDate().equals(course.getEndDate())
							&& c.getCourse().getEndTime().equals(course.getEndTime())
							&& c.getCourse().getLocation().equalsIgnoreCase(course.getLocation())
							&& c.getStudent().getFirstName().equalsIgnoreCase(student.getFirstName())
							&& c.getStudent().getLastName().equalsIgnoreCase(student.getLastName()))
					.findAny().orElse(null);
			if (enroll != null) {
				throw new StudentAlreadyEnrolledInCourseException("Student already registered in the course");
			}

			LOGGER.info("Student " + student.getFirstName() + " enrolled for the course " + course.getCourseName());
			courseEnrollments
					.add(CourseEnrollment.build(courseEnroll -> courseEnroll.setStudent(student).setCourse(course)));
		}
	}

	public List<Pair<Course, Course>> FindCoursesToCombine(int maximumStudentThreshold) {

		Map<Course, List<Student>> courseMap = new HashMap<Course, List<Student>>();

		courseEnrollments.stream().forEach(c -> {
			if (!isMapContainsCourse(courseMap, c.getCourse(), c.getStudent())) {
				List<Student> students = new ArrayList<Student>();
				students.add(c.getStudent());
				courseMap.put(c.getCourse(), students);
			}

		});

		List<Course> elligibleCoursesToCombine = new ArrayList<Course>();
		courseMap.forEach((k, v) -> {
			if (k.getCapacity() != v.size() && v.size() < maximumStudentThreshold) {
				LOGGER.info("Elligible Course Capacity " + k.getCapacity() + " Student - " + v.size());
				k.setCapacity(v.size());
				elligibleCoursesToCombine.add(k);
			}
		});

		List<Pair<Course, Course>> pairCourses = new ArrayList<Pair<Course, Course>>();

		List<Course> sortedCourses = elligibleCoursesToCombine.stream()
				.sorted(Comparator.comparingInt(Course::getCapacity).reversed()).collect(Collectors.toList());

		sortedCourses.stream().forEach(c -> LOGGER.info(c.getCapacity()));

		int reverseIndex = sortedCourses.size() - 1;
		for (int index = 0; index < sortedCourses.size(); index++) {
			Course course1 = sortedCourses.get(index);

			for (; reverseIndex > index; reverseIndex--) {
				Course course2 = sortedCourses.get(reverseIndex);
				LOGGER.info("course1 - " + course1.getCapacity());
				LOGGER.info("course2 - " + course2.getCapacity());
				if ((course2.getCapacity() + course1.getCapacity()) <= maximumStudentThreshold) {
					Pair<Course, Course> pair = new MutablePair<>(course1, course2);
					pairCourses.add(pair);
				} else {
					break;
				}
			}
		}

		return pairCourses;
	}

	private boolean isMapContainsCourse(Map<Course, List<Student>> courseMap, Course course, Student student) {

		Set<Course> courses = courseMap.keySet();
		for (Course c : courses) {
			if (c.getCourseName().equalsIgnoreCase(course.getCourseName())
					&& c.getLocation().equalsIgnoreCase(course.getLocation())
					&& c.getStartDate().equals(course.getStartDate()) && c.getEndDate().equals(course.getEndDate())
					&& c.getEndTime().equals(course.getEndTime()) && c.getCapacity() == course.getCapacity()
					&& c.getDayNumber() == course.getDayNumber()) {
				courseMap.get(c).add(student);
				return true;
			}
		}

		return false;
	}
}
