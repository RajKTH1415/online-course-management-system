package com.ocms.controller;

import com.ocms.dtos.ReviewRequest;
import com.ocms.service.CourseService;
import com.ocms.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasRole('STUDENT')")
@Tag(name = "Student API Controller", description = "Endpoints for students to enroll, complete lessons, and review courses")
public class StudentController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private EnrollmentService enrollmentService;


    @Operation(
            summary = "Get all approved courses",
            description = "Fetches a list of all courses that have been approved and are available for students to enroll in.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of approved courses returned successfully")
            }
    )
    @GetMapping("/courses")
    public ResponseEntity<?> getAllCourses() { return ResponseEntity.ok(courseService.getAllApprovedCourses()); }



    @Operation(
            summary = "Enroll in a course",
            description = "Enrolls the authenticated student into a specific course by course ID.",
            parameters = {
                    @Parameter(name = "id", description = "Course ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Student enrolled successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid course ID or already enrolled")
            }
    )
    @PostMapping("/courses/{id}/enroll")
    public ResponseEntity<?> enroll(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(enrollmentService.enroll(id, principal.getName()));
    }

    @Operation(
            summary = "Mark lesson as completed",
            description = "Marks a lesson within a course as completed for the authenticated student.",
            parameters = {
                    @Parameter(name = "courseId", description = "Course ID", required = true),
                    @Parameter(name = "lessonId", description = "Lesson ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lesson marked as completed successfully"),
                    @ApiResponse(responseCode = "404", description = "Course or Lesson not found")
            }
    )
    @PostMapping("/courses/{courseId}/lessons/{lessonId}/complete")
    public ResponseEntity<?> completeLesson(@PathVariable Long courseId, @PathVariable Long lessonId, Principal principal) {
        enrollmentService.completeLesson(courseId, lessonId, principal.getName());
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "Add a course review",
            description = "Allows a student to add a review for a specific course.",
            parameters = {
                    @Parameter(name = "id", description = "Course ID", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Review details including rating and comments",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ReviewRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review added successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request body or course ID")
            }
    )
    @PostMapping("/courses/{id}/review")
    public ResponseEntity<?> addReview(@PathVariable Long id, @RequestBody ReviewRequest request, Principal principal) {
        return ResponseEntity.ok(courseService.addReview(id, request, principal.getName()));
    }
}