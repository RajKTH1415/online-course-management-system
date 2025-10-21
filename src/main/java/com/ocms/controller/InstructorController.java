package com.ocms.controller;

import com.ocms.dtos.ApiResponse;
import com.ocms.dtos.CourseRequest;
import com.ocms.entity.Course;
import com.ocms.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/instructor")
@PreAuthorize("hasRole('INSTRUCTOR')")
@Tag(name = "Instructor API Controller", description = "Endpoints for instructors to manage their courses")
public class InstructorController {
    @Autowired
    private CourseService courseService;


    @Operation(summary = "Create a new course", description = "Instructor can create a new course")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Course created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping("/courses")
    public ResponseEntity<?> createCourse(@RequestBody CourseRequest request, Principal principal) {
        return ResponseEntity.ok(courseService.createCourse(request, principal.getName()));
    }

    @Operation(summary = "Update a course", description = "Instructor can update an existing course they own")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Course updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found", content = @Content)
    })
    @PutMapping("/courses/{id}")
    public ResponseEntity<?> updateCourse(
            @PathVariable Long id,
            @RequestBody CourseRequest request,
            Principal principal) {
        return ResponseEntity.ok(courseService.updateCourse(id, request, principal.getName()));
    }

    @Operation(summary = "Get enrolled students", description = "Fetch list of students enrolled in a specific course")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Students retrieved successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found", content = @Content)
    })
    @GetMapping("/courses/{id}/students")
    public ResponseEntity<?> getEnrolledStudents(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(courseService.getEnrolledStudents(id));
    }


    @Operation(summary = "Delete a course", description = "Instructor can delete a course they own")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Course deleted successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found", content = @Content)
    })
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long id, Principal principal) {
        courseService.deleteCourse(id, principal.getName());
        ApiResponse<Void> response = new ApiResponse<>(
                true,
                "Course deleted successfully",
                null
        );
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Get course by ID", description = "Fetch details of a course by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Course retrieved successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found", content = @Content)
    })
    @GetMapping("/courses/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));

    }
}