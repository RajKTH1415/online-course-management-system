
package com.ocms.service;

import com.ocms.dtos.CourseRequest;
import com.ocms.dtos.ReviewRequest;
import com.ocms.entity.Course;
import com.ocms.entity.User;

import java.util.List;

public interface CourseService {
    Course createCourse(CourseRequest req, String instructorEmail);

    Course updateCourse(Long courseId, CourseRequest req, String instructorEmail);

    void deleteCourse(Long courseId, String instructorEmail);

    Course getCourseById(Long courseId);

    List<Course> getAllApprovedCourses();

    void approveCourse(Long courseId);

    void rejectCourse(Long courseId);

    List<User> getEnrolledStudents(Long courseId);

    Course addReview(Long courseId, ReviewRequest req, String studentEmail);
}
