package com.ocms.controller;

import com.ocms.service.Impl.CourseServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courses")
public class PublicController {

    private final CourseServiceImpl courseService;

    public PublicController(CourseServiceImpl courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    ResponseEntity<?>getAllApprovedCourses(@RequestParam(required = false) String status){
//        if ("APPROVED".equalsIgnoreCase(status)){
//            return ResponseEntity.ok(courseService.getAllApprovedCourses());
//        }
//        return ResponseEntity.ok(courseService.getAllApprovedCourses());
        if (status == null){
            return ResponseEntity.ok(courseService.getAllCourses());
        }
        switch (status.toUpperCase()){
            case "APPROVED":
                return ResponseEntity.ok(courseService.getAllApprovedCourses());
            case "REJECTED":
                return ResponseEntity.ok(courseService.getAllRejectedCourse());
            case "PENDING":
                return ResponseEntity.ok(courseService.getAllPendingCourses());
            default:
                return ResponseEntity.badRequest().body("Invalid status");
        }

    }
}
