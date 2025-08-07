package com.ocms.controller;

import com.ocms.dtos.ReviewRequest;
import com.ocms.service.CourseService;
import com.ocms.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/courses")
    public ResponseEntity<?> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllApprovedCourses());
    }

    @PostMapping("/courses/{id}/enroll")
    public ResponseEntity<?> enroll(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(enrollmentService.enroll(id, principal.getName()));
    }

    @PostMapping("/courses/{courseId}/lessons/{lessonId}/complete")
    public ResponseEntity<?> completeLesson(@PathVariable Long courseId, @PathVariable Long lessonId, Principal principal) {
        enrollmentService.completeLesson(courseId, lessonId, principal.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/courses/{id}/review")
    public ResponseEntity<?> addReview(@PathVariable Long id, @RequestBody ReviewRequest request, Principal principal) {
        return ResponseEntity.ok(courseService.addReview(id, request, principal.getName()));
    }
}