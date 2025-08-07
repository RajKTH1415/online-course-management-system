package com.ocms.service.Impl;

import com.ocms.dtos.CourseRequest;
import com.ocms.dtos.ReviewRequest;
import com.ocms.entity.Course;
import com.ocms.entity.Enrollment;
import com.ocms.entity.Review;
import com.ocms.entity.User;
import com.ocms.enums.CourseStatus;
import com.ocms.exception.CustomException;
import com.ocms.repository.CourseRepository;
import com.ocms.repository.EnrollmentRepository;
import com.ocms.repository.ReviewRepository;
import com.ocms.repository.UserRepository;
import com.ocms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {


    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ReviewRepository reviewRepository;


    @Override
    public Course createCourse(CourseRequest request, String instructorEmail) {
        User instructor = userRepository.findByEmail(instructorEmail)
                .orElseThrow(() -> new CustomException("Instructor not found"));
        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .level(request.getLevel())
                .price(request.getPrice())
                .instructor(instructor)
                .status(CourseStatus.PENDING)
                .build();
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllApprovedCourses() {
        return courseRepository.findByStatus(CourseStatus.APPROVED);
    }

    @Override
    public void approveCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CustomException("Course not found"));
        course.setStatus(CourseStatus.APPROVED);
        courseRepository.save(course);
    }

    @Override
    public void rejectCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CustomException("Course not found"));
        course.setStatus(CourseStatus.REJECTED);
        courseRepository.save(course);
    }

    @Override
    public List<User> getEnrolledStudents(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException("Course not found"));
        List<Enrollment> enrollments = enrollmentRepository.findByCourse(course);
        List<User> students = new ArrayList<>();
        for (Enrollment e : enrollments) {
            students.add(e.getStudent());
        }
        return students;
    }

    @Override
    public Review addReview(Long courseId, ReviewRequest request, String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new CustomException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException("Course not found"));
        Review review = Review.builder()
                .student(student)
                .course(course)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();
        return reviewRepository.save(review);
    }

    @Override
    public Course updateCourse(Long courseId, CourseRequest request, String instructorEmail) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException("Course not found"));

        if (!course.getInstructor().getEmail().equals(instructorEmail)) {
            throw new CustomException("Unauthorized to update this course");
        }

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCategory(request.getCategory());
        course.setLevel(request.getLevel());
        course.setPrice(request.getPrice());
        course.setStatus(CourseStatus.PENDING);

        return courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Long courseId, String instructorEmail) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException("Course not found"));

        if (!course.getInstructor().getEmail().equals(instructorEmail)) {
            throw new CustomException("Unauthorized to delete this course");
        }

        courseRepository.delete(course);
    }
}
