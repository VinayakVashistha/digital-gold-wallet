package com.example.demo.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {

        String rootMessage = ex.getRootCause() != null
                ? ex.getRootCause().getMessage()
                : ex.getMessage();

        String message = "Database constraint violation";

        if (rootMessage != null) {
            String lower = rootMessage.toLowerCase();

            if (lower.contains("duplicate entry") && lower.contains("email")) {
                message = "A user with this email already exists";
            } else if (lower.contains("duplicate entry")) {
                message = "This record already exists";
            } else if (lower.contains("foreign key")) {
                message = "Invalid linked record. Please provide a valid user, vendor, branch, or address id";
            } else if (lower.contains("cannot be null") || lower.contains("not-null")) {
                message = "Required field is missing";
            }
        }

        return buildResponse(
                HttpStatus.CONFLICT,
                message,
                "Please check your input and try again"
        );
    }

    @ExceptionHandler({
            ResourceNotFoundException.class,
            EntityNotFoundException.class,
            NoResourceFoundException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFound(Exception ex) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Requested resource not found",
                "The id or endpoint you are trying to access does not exist"
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidJson(HttpMessageNotReadableException ex) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid request body",
                "Please send valid JSON data"
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        String expectedType = ex.getRequiredType() != null
                ? ex.getRequiredType().getSimpleName()
                : "valid type";

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid value for '" + ex.getName() + "'",
                "Expected type: " + expectedType
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParam(
            MissingServletRequestParameterException ex) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Missing required parameter: " + ex.getParameterName(),
                "Please provide all required request parameters"
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleWrongMethod(
            HttpRequestMethodNotSupportedException ex) {

        return buildResponse(
                HttpStatus.METHOD_NOT_ALLOWED,
                "HTTP method not allowed",
                "Use the correct HTTP method for this endpoint"
        );
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Map<String, Object>> handleNumberFormat(NumberFormatException ex) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid number format",
                "Please enter a valid numeric value"
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong",
                "Please try again later"
        );
    }

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String message,
            String details) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("details", details);

        return new ResponseEntity<>(body, status);
    }
}