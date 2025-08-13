
package com.ocms.service.Impl;

import com.ocms.dtos.CourseRequest;
import com.ocms.dtos.ReviewRequest;
import com.ocms.entity.*;
import com.ocms.enums.CourseStatus;
import com.ocms.enums.Role;
import com.ocms.exception.CustomException;
import com.ocms.repository.*;
import com.ocms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private ReviewRepository reviewRepository;


    @Override
    @Transactional
    public Course createCourse(CourseRequest req, String instructorEmail) {

        User instructor = userRepository.findByEmail(instructorEmail).orElseThrow(() -> new CustomException("Instructor not found"));
        if (instructor.getRole() != Role.INSTRUCTOR && instructor.getRole() != Role.ADMIN)
            throw new CustomException("Only instructors can create courses");
        Course c = Course.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .category(req.getCategory())
                .level(req.getLevel())
                .price(req.getPrice())
                .instructor(instructor)
                .status(CourseStatus.PENDING)
                .build();
        List<Lesson> lessons = Optional.ofNullable(req.getLessons()).orElse(Collections.emptyList())
                .stream()
                .map(l -> Lesson.builder().title(l.getTitle()).videoUrl(l.getVideoUrl()).duration(l.getDuration()).course(c).build())
                .collect(Collectors.toList());
        c.setLessons(lessons);
        return courseRepository.save(c);
    }

    @Override
    public Course updateCourse(Long courseId, CourseRequest req, String instructorEmail) {
        Course c = courseRepository.findById(courseId).orElseThrow(() -> new CustomException("Course not found"));
        if (!c.getInstructor().getEmail().equals(instructorEmail) && c.getInstructor().getRole()!=Role.ADMIN) {
            throw new CustomException("Not authorized to edit");
        }
        c.setTitle(req.getTitle());
        c.setDescription(req.getDescription());
        c.setCategory(req.getCategory());
        c.setLevel(req.getLevel());
        c.setPrice(req.getPrice());
        // reset lessons
        c.getLessons().clear();
        List<Lesson> lessons = Optional.ofNullable(req.getLessons()).orElse(Collections.emptyList())
                .stream()
                .map(l -> Lesson.builder().title(l.getTitle()).videoUrl(l.getVideoUrl()).duration(l.getDuration()).course(c).build())
                .collect(Collectors.toList());
        c.getLessons().addAll(lessons);
        c.setStatus(CourseStatus.PENDING);
        return courseRepository.save(c);
    }

    @Override
    public void deleteCourse(Long courseId, String instructorEmail) {
        Course c = courseRepository.findById(courseId).orElseThrow(() -> new CustomException("Course not found"));
        if (!c.getInstructor().getEmail().equals(instructorEmail) && c.getInstructor().getRole()!=Role.ADMIN) {
            throw new CustomException("Not authorized to delete");
        }
        courseRepository.delete(c);
    }

    @Override
    public List<Course> getAllApprovedCourses() {
        return courseRepository.findByStatus(CourseStatus.APPROVED);
    }

    @Override
    public void approveCourse(Long courseId) {
        Course c = courseRepository.findById(courseId).orElseThrow(() -> new CustomException("Course not found"));
        c.setStatus(CourseStatus.APPROVED);
        courseRepository.save(c);
    }

    @Override
    public void rejectCourse(Long courseId) {
        Course c = courseRepository.findById(courseId).orElseThrow(() -> new CustomException("Course not found"));
        c.setStatus(CourseStatus.REJECTED);
        courseRepository.save(c);
    }

    @Override
    public List<User> getEnrolledStudents(Long courseId) {
        Course c = courseRepository.findById(courseId).orElseThrow(() -> new CustomException("Course not found"));
        var enrollments = enrollmentRepository.findByCourse(c);
        List<User> students = new ArrayList<>();
        enrollments.forEach(e -> students.add(e.getStudent()));
        return students;
    }

    @Override
    public Course addReview(Long courseId, ReviewRequest req, String studentEmail) {
        User student = userRepository.findByEmail(studentEmail).orElseThrow(() -> new CustomException("Student not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new CustomException("Course not found"));
        Review r = Review.builder().rating(req.getRating()).comment(req.getComment()).student(student).course(course).build();
        reviewRepository.save(r);
        course.getReviews().add(r);
        return courseRepository.save(course);
    }
}
