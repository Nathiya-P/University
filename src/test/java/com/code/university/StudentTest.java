package com.code.university;

import static com.code.university.util.Constants.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.code.university.Exception.CourseAtCapacityException;
import com.code.university.Exception.StudentAlreadyEnrolledInCourseException;
import com.code.university.model.Course;
import com.code.university.model.Student;
import com.code.university.service.RegisterService;
import com.code.university.util.Stopwatch;
import com.code.university.util.Utility;

@RunWith(MockitoJUnitRunner.class)
public class StudentTest {

	private static Logger LOGGER = Logger.getLogger(StudentTest.class);

	@Test
	public void shouldRegisterStudent() {

		RegisterService registerService = new RegisterService();
		Student student = Student.build(stud -> stud.setFirstName(Utility.getFirstNameString()))
				.setLastName(Utility.getLastNameString()).setGpa(Utility.getGPADouble(MIN_GPA, MAX_GPA));

		registerService.registerStudent(student);
		assertTrue(registerService.getRegisteredStudents().size() == 1);
	}

	@Test
	public void shouldRegisterStudents() {
		RegisterService registerService = new RegisterService();
		List<Student> students = register_Students(10, 2.0, 4.3);
		registerService.registerStudents(students);
		assertThat(registerService.getRegisteredStudents(), is(students));
	}

	@Test
	public void shouldEnrollStudentInCourse()
			throws StudentAlreadyEnrolledInCourseException, CourseAtCapacityException {

		RegisterService registerService = new RegisterService();

		Student student = Student.build(stud -> stud.setFirstName(Utility.getFirstNameString()))
				.setLastName(Utility.getLastNameString()).setGpa(Utility.getGPADouble(MIN_GPA, MAX_GPA));

		Course course = Course.build(cr -> cr.setCourseName(Utility.getCourseString()))
				.setDayNumber(DayOfWeek.of(Utility.createRandomIntBetween(1, 7)))
				.setStartDate(Utility.getRandomDate(MIN_YEAR, MID_YEAR)).setStartTime(Utility.getRandomTime())
				.setEndDate(Utility.getRandomDate(MID_YEAR, MAX_YEAR)).setEndTime(Utility.getRandomTime())
				.setCapacity(CAPACITY).setLocation(Utility.getLocationString());

		registerService.enrollStudentInCourse(student, course);

		assertNotNull(registerService.getStudentsEnrolledInCourse(course));
	}

	@Test
	public void shouldDropStudentFromCourse()
			throws StudentAlreadyEnrolledInCourseException, CourseAtCapacityException {

		RegisterService registerService = new RegisterService();
		Student student = Student.build(stud -> stud.setFirstName(Utility.getFirstNameString()))
				.setLastName(Utility.getLastNameString()).setGpa(Utility.getGPADouble(MIN_GPA, MAX_GPA));

		Course course = Course.build(cr -> cr.setCourseName(Utility.getCourseString()))
				.setDayNumber(DayOfWeek.of(Utility.createRandomIntBetween(1, 7)))
				.setStartDate(Utility.getRandomDate(MIN_YEAR, MID_YEAR)).setStartTime(Utility.getRandomTime())
				.setEndDate(Utility.getRandomDate(MID_YEAR, MAX_YEAR)).setEndTime(Utility.getRandomTime())
				.setCapacity(CAPACITY).setLocation(Utility.getLocationString());

		registerService.enrollStudentInCourse(student, course);

		registerService.dropStudentFromCourse(student, course);
		assertThat(registerService.getStudentsEnrolledInCourse(course), IsEmptyCollection.empty());
	}

	@Test(expected = CourseAtCapacityException.class)
	public void shouldThrowCourseAtCapacityException()
			throws StudentAlreadyEnrolledInCourseException, CourseAtCapacityException {
		RegisterService registerService = new RegisterService();
		Course course = Course.build(cr -> cr.setCourseName(Utility.getCourseString()).setCapacity(1))
				.setDayNumber(DayOfWeek.of(Utility.createRandomIntBetween(1, 7)))
				.setStartDate(Utility.getRandomDate(MIN_YEAR, MID_YEAR)).setStartTime(Utility.getRandomTime())
				.setEndDate(Utility.getRandomDate(MID_YEAR, MAX_YEAR)).setEndTime(Utility.getRandomTime())
				.setLocation(Utility.getLocationString());

		Student student = Student.build(stud -> stud.setFirstName(Utility.getFirstNameString()))
				.setLastName(Utility.getLastNameString()).setGpa(Utility.getGPADouble(MIN_GPA, MAX_GPA));

		registerService.enrollStudentInCourse(student, course);

		Student student2 = Student.build(stud -> stud.setFirstName(Utility.getFirstNameString()))
				.setLastName(Utility.getLastNameString()).setGpa(Utility.getGPADouble(MIN_GPA, MAX_GPA));

		registerService.enrollStudentInCourse(student2, course);
	}

	@Test(expected = StudentAlreadyEnrolledInCourseException.class)
	public void shouldThrowStudentAlreadyEnrolledInCourseException()
			throws StudentAlreadyEnrolledInCourseException, CourseAtCapacityException {
		RegisterService registerService = new RegisterService();
		Course course = Course.build(cr -> cr.setCourseName(Utility.getCourseString()).setCapacity(3))
				.setDayNumber(DayOfWeek.of(Utility.createRandomIntBetween(1, 7)))
				.setStartDate(Utility.getRandomDate(MIN_YEAR, MID_YEAR)).setStartTime(Utility.getRandomTime())
				.setEndDate(Utility.getRandomDate(MID_YEAR, MAX_YEAR)).setEndTime(Utility.getRandomTime())
				.setLocation(Utility.getLocationString());
		Student student = Student.build(stud -> stud.setFirstName(Utility.getFirstNameString()))
				.setLastName(Utility.getLastNameString()).setGpa(Utility.getGPADouble(MIN_GPA, MAX_GPA));

		registerService.enrollStudentInCourse(student, course);

		registerService.enrollStudentInCourse(student, course);
	}

	@Test
	public void courseEnrollmentShouldBeThreadSafe()
			throws StudentAlreadyEnrolledInCourseException, CourseAtCapacityException {
		int numberOfTestIterations = 100;
		List<Boolean> iterationResults = new ArrayList<Boolean>();
		for (int i = 1; i <= numberOfTestIterations; i++) {
			RegisterService registerService = new RegisterService();
			Course course = Course.build(cr -> cr.setCourseName(Utility.getCourseString()).setCapacity(2))
					.setDayNumber(DayOfWeek.of(Utility.createRandomIntBetween(1, 7)))
					.setStartDate(Utility.getRandomDate(MIN_YEAR, MID_YEAR)).setStartTime(Utility.getRandomTime())
					.setEndDate(Utility.getRandomDate(MID_YEAR, MAX_YEAR)).setEndTime(Utility.getRandomTime())
					.setLocation(Utility.getLocationString());
			Student student = Student.build(stud -> stud.setFirstName(Utility.getFirstNameString()))
					.setLastName(Utility.getLastNameString()).setGpa(Utility.getGPADouble(MIN_GPA, MAX_GPA));

			registerService.enrollStudentInCourse(student, course);

			int studentCount = 10;
			int expectedExceptionCount = studentCount
					- (course.getCapacity() - registerService.getStudentsEnrolledInCourse(course).size());
			int[] actualExceptionCount = { 0 };

			List<Thread> threads = new ArrayList<Thread>();

			for (int count = 0; count < studentCount; count++) {
				Thread thread = new Thread(() -> {
					Student student1 = Student.build(stud -> stud.setFirstName(Utility.getFirstNameString()))
							.setLastName(Utility.getLastNameString()).setGpa(Utility.getGPADouble(MIN_GPA, MAX_GPA));

					try {
						registerService.enrollStudentInCourse(student1, course);
					} catch (CourseAtCapacityException ex) {
						actualExceptionCount[0] = actualExceptionCount[0] + 1;
					} catch (StudentAlreadyEnrolledInCourseException e) {
						e.printStackTrace();
					}
				});
				thread.start();
				threads.add(thread);
			}

			threads.stream().forEach(th -> {
				try {
					th.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});

			iterationResults.add(actualExceptionCount[0] == expectedExceptionCount);
		}
		assertFalse(iterationResults.contains(false));
	}

	@Test
	public void speedTest_CalculateNumberOfStudentsWithMinimumGpaOf() {

		RegisterService registerService = new RegisterService();
		setupStudentsForGpaCalculationTests(registerService, 200000, .35, .4, .25);

		List<Long> intervals = new ArrayList<Long>();

		for (int i = 0; i < 100; i++) {
			Stopwatch stopWatch = new Stopwatch();
			stopWatch.start();
			int count = registerService.calculateNumberOfStudentsWithMinimumGpaOf(Utility.getGPADouble(2.0, 4.3));
			stopWatch.stop();
			intervals.add(stopWatch.getElapsedTicks());
		}

		List<Long> intervals_SkippedOutliers = Utility.skipOutliers(intervals);
		LongSummaryStatistics stats = intervals_SkippedOutliers.stream().mapToLong((x) -> x).summaryStatistics();

		LOGGER.info("Average time - " + stats.getAverage());
		assertTrue(stats.getAverage() <= 20000);

	}

	public List<Student> register_Students(int numberOfStudents, double minGPA, double maxGPA) {

		List<Student> studentsList = new ArrayList<Student>();
		for (int i = 0; i < numberOfStudents; i++) {
			Student student = Student.build(stud -> stud.setFirstName(Utility.getFirstNameString()))
					.setLastName(Utility.getLastNameString()).setGpa(Utility.getGPADouble(minGPA, maxGPA));

			studentsList.add(student);
		}

		return studentsList;

	}

	private void setupStudentsForGpaCalculationTests(RegisterService registerService, int numberOfStudentsToAdd,
			double percentageOfGpa4Plus, double percentageOfGpa3To4, double percentageOfGpa2To3) {

		List<Student> studentListwithGpa4Plus = register_Students((int) (numberOfStudentsToAdd * percentageOfGpa4Plus),
				4.0, 4.3);
		registerService.registerStudents(studentListwithGpa4Plus);
		List<Student> studentListwithGpa3To4 = register_Students((int) (numberOfStudentsToAdd * percentageOfGpa3To4),
				3.0, 3.9);
		registerService.registerStudents(studentListwithGpa3To4);
		List<Student> studentListwithGpa2To3 = register_Students((int) (numberOfStudentsToAdd * percentageOfGpa2To3),
				2.0, 2.9);
		registerService.registerStudents(studentListwithGpa2To3);
	}

	@Test
	public void findCoursesToCombine() throws StudentAlreadyEnrolledInCourseException, CourseAtCapacityException {
		RegisterService registerService = new RegisterService();
		int[] coursesCapacity = { 12, 6, 9, 7, 11, 8, 12 };
		int[] numberOfStudentEnrolled = { 9, 4, 5, 7, 5, 6, 7 };

		for (int i = 0; i < coursesCapacity.length; i++) {
			Course course = Course.build(cr -> cr.setCourseName(Utility.getCourseString()))
					.setDayNumber(DayOfWeek.of(Utility.createRandomIntBetween(1, 7)))
					.setStartDate(Utility.getRandomDate(MIN_YEAR, MID_YEAR)).setStartTime(Utility.getRandomTime())
					.setEndDate(Utility.getRandomDate(MID_YEAR, MAX_YEAR)).setEndTime(Utility.getRandomTime())
					.setLocation(Utility.getLocationString());
			course.setCapacity(coursesCapacity[i]);
			for (int j = 0; j < numberOfStudentEnrolled[i]; j++) {
				Student student = Student.build(stud -> stud.setFirstName(Utility.getFirstNameString()))
						.setLastName(Utility.getLastNameString()).setGpa(Utility.getGPADouble(MIN_GPA, MAX_GPA));

				registerService.enrollStudentInCourse(student, course);
			}
		}

		int maximumStudentThreshold = 10;
		List<Pair<Course, Course>> coursePairs = registerService.FindCoursesToCombine(maximumStudentThreshold);
		LOGGER.info("PAIRS - " + coursePairs.size());
		coursePairs.stream().forEach(p -> {
			LOGGER.info("COURSE 1 - " + p.getKey().getCapacity());
			LOGGER.info("COURSE 1 - " + p.getValue().getCapacity());
		});

		assertTrue(coursePairs.size() == 2);
	}

}
