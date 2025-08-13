package com.ocms.controller;

import com.ocms.dtos.CourseRequest;
import com.ocms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/instructor")
@PreAuthorize("hasRole('INSTRUCTOR')")
public class InstructorController {
    @Autowired
    private CourseService courseService;

    @PostMapping("/courses")
    public ResponseEntity<?> createCourse(@RequestBody CourseRequest request, Principal principal) {
        return ResponseEntity.ok(courseService.createCourse(request, principal.getName()));
    }

    @GetMapping("/courses/{id}/students")
    public ResponseEntity<?> getEnrolledStudents(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(courseService.getEnrolledStudents(id));
    }
}