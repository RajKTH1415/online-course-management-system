package com.ocms.service;

import com.ocms.entity.Enrollment;

public interface EnrollmentService {

    Enrollment enroll(Long courseId, String studentEmail);
    void completeLesson(Long courseId, Long lessonId, String studentEmail);
}
