package com.example.demo.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // =========================================================================
    // 1. Resource Not Found — GET /users/999999
    // =========================================================================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(
            ResourceNotFoundException ex) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Resource not found",
                ex.getMessage()
        );
    }

    // =========================================================================
    // 2. Duplicate Entry — POST /users with existing email
    // =========================================================================
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {

        String message = "Data integrity violation";

        // Extract readable cause — e.g. "Duplicate entry 'x@y.com' for key 'users.email'"
        if (ex.getRootCause() != null) {
            String rootMsg = ex.getRootCause().getMessage();
            if (rootMsg != null && rootMsg.contains("Duplicate entry")) {
                message = "Duplicate value: " + rootMsg;
            } else if (rootMsg != null) {
                message = rootMsg;
            }
        }

        return buildResponse(
                HttpStatus.CONFLICT,
                message,
                "A record with this value already exists"
        );
    }

    // =========================================================================
    // 3. Empty or Malformed JSON Body — POST /users with empty body
    // =========================================================================
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleMessageNotReadable(
            HttpMessageNotReadableException ex) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Request body is missing or malformed",
                "Please provide a valid JSON body"
        );
    }

    // =========================================================================
    // 4. Wrong Type in URL — GET /users/abc instead of /users/1
    // =========================================================================
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid value '" + ex.getValue() + "' for parameter '" + ex.getName() + "'",
                "Expected type: " + ex.getRequiredType().getSimpleName()
        );
    }

    // =========================================================================
    // 5. URL Does Not Exist — GET /api/v1/wrongpath
    // =========================================================================
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFound(
            NoResourceFoundException ex) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Endpoint not found",
                ex.getMessage()
        );
    }

    // =========================================================================
    // 6. Illegal Argument — e.g. passing null where not allowed
    // =========================================================================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid argument",
                ex.getMessage()
        );
    }

    // =========================================================================
    // 7. Catch-All — anything else that slips through
    //    This is the one that kills stack traces completely
    // =========================================================================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex) {

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                "Please contact support if this persists"
                // NOTE: Never expose ex.getMessage() here in production
                // It would leak stack trace info to the user
        );
    }

    // =========================================================================
    // Helper — builds consistent JSON response for all errors
    // =========================================================================
    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status, String message, String details) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("details", details);

        return new ResponseEntity<>(body, status);
    }
}