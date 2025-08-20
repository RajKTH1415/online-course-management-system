package com.ocms.controller;

import com.ocms.service.Impl.CourseServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "Public Course API Controller", description = "Endpoints for fetching public course details")
public class PublicController {

    private final CourseServiceImpl courseService;

    public PublicController(CourseServiceImpl courseService) {
        this.courseService = courseService;
    }

    @Operation(
            summary = "Get courses by status",
            description = "Fetches courses based on status. " +
                    "If no status is provided, it returns all courses. " +
                    "Available status values: APPROVED, REJECTED, PENDING."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status",
                    content = @Content(schema = @Schema(example = "Invalid status")))
    })
    @GetMapping
    ResponseEntity<?> getAllApprovedCourses(
            @Parameter(description = "Course status filter (APPROVED, REJECTED, PENDING)", required = false) @RequestParam(required = false) String status) {
//        if ("APPROVED".equalsIgnoreCase(status)){
//            return ResponseEntity.ok(courseService.getAllApprovedCourses());
//        }
//        return ResponseEntity.ok(courseService.getAllApprovedCourses());
        if (status == null) {
            return ResponseEntity.ok(courseService.getAllCourses());
        }
        switch (status.toUpperCase()) {
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
