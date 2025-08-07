package com.ocms.service.Impl;

import com.ocms.entity.Course;
import com.ocms.entity.Enrollment;
import com.ocms.entity.User;
import com.ocms.exception.CustomException;
import com.ocms.repository.CourseRepository;
import com.ocms.repository.EnrollmentRepository;
import com.ocms.repository.UserRepository;
import com.ocms.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {


    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;


    @Override
    public Enrollment enroll(Long courseId, String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new CustomException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException("Course not found"));
        if (enrollmentRepository.findByStudentAndCourse(student, course).isPresent())
            throw new CustomException("Already enrolled");
        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .paymentDone(true)
                .build();
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public void completeLesson(Long courseId, Long lessonId, String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new CustomException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException("Course not found"));
        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new CustomException("Not enrolled"));
        enrollment.getCompletedLessonIds().add(lessonId);
        enrollmentRepository.save(enrollment);
    }
}
