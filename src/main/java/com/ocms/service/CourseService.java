package com.ocms.service;

import com.ocms.dtos.CourseRequest;
import com.ocms.dtos.ReviewRequest;
import com.ocms.entity.Course;
import com.ocms.entity.Review;
import com.ocms.entity.User;

import java.util.List;

public interface CourseService {

    Course createCourse(CourseRequest request, String instructorEmail);

    List<Course> getAllApprovedCourses();

    void approveCourse(Long id);

    void rejectCourse(Long id);

    List<User> getEnrolledStudents(Long courseId);

    Review addReview(Long courseId, ReviewRequest request, String studentEmail);
}
