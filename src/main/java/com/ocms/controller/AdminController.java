package com.ocms.controller;

import com.ocms.dtos.ApiResponse;
import com.ocms.entity.User;
import com.ocms.service.CourseService;
import com.ocms.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin API Controller", description = "Endpoints for course approval/rejection and user management (Admin only)")
public class AdminController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;


    @Operation(
            summary = "Approve a course",
            description = "Approves a course submitted by an instructor (Admin only)."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Course approved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found", content = @Content)
    })
    @PutMapping("/courses/{id}/approve")
    public ResponseEntity<?> approveCourse(@PathVariable Long id) {
        courseService.approveCourse(id);
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "Reject a course",
            description = "Rejects a course submitted by an instructor (Admin only)."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Course rejected successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found", content = @Content)
    })
    @PutMapping("/courses/{id}/reject")
    public ResponseEntity<?> rejectCourse(@PathVariable Long id) {
        courseService.rejectCourse(id);
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "Block a user",
            description = "Blocks a user account so they can no longer access the system (Admin only)."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User blocked successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping("/users/{id}/block")
    public ResponseEntity<ApiResponse<User>> blockUser(@PathVariable Long id) {
        User blockUser = userService.blockUser(id);
        ApiResponse<User> response = new ApiResponse<>(
                true,
                "User blocked successfully",
                blockUser
        );
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Unblock a user",
            description = "Unblocks a previously blocked user account (Admin only)."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User unblocked successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping("/users/{id}/unblock")
    public ResponseEntity<ApiResponse<User>> unblockUser(@PathVariable Long id) {
        User unblockUser = userService.unblockUser(id);
        ApiResponse<User> response = new ApiResponse<>(
                true,
                "User unblock successfully",
                unblockUser
        );
        return ResponseEntity.ok(response);
    }
}