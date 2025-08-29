package com.ocms.exception;

import com.ocms.dtos.ApiResponse;
import com.ocms.dtos.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({ org.springframework.security.core.AuthenticationException.class })
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception ex,
                                                                       HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.name(),
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid or missing token",
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex,
                                                               HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.name(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex,
                                                                HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.name(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex,
                                                            HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.FORBIDDEN.name(),
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }


//    @ExceptionHandler(CustomException.class)
//    public ResponseEntity<?> handleCustom(CustomException ex) {
//        //return ResponseEntity.badRequest().body(ex.getMessage());
////        ApiResponse<Object> errorResponse = new ApiResponse<>(false,
////                ex.getMessage(),null,"COURSE_NOT_FOUND");
////        errorResponse.setTimestamp(LocalDateTime.now());
////        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(ApiResponse.error(ex.getMessage(), "COURSE_NOT_FOUND"));
//    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFound(UsernameNotFoundException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                false,
                ex.getMessage(),
                null,
                "USER_NOT_FOUND"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                false,
                "Invalid username or password",
                null,
                "BAD_CREDENTIALS"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(Exception ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                false,
                "Something went wrong",
                null,
                "INTERNAL_ERROR"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ApiResponse<Object> response = new ApiResponse<>(
                false,
                "Validation failed",
                errors,
                "VALIDATION_ERROR"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}