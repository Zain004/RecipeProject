package com.example.demo;

import com.example.demo.recipe.APiAndDtos.APiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APiResponse<Object>> handleValidationException(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage) //
                .collect(Collectors.toList()); // samler de resulterende feilmeldingene i string
        logger.warn("Validation failed: {}, Message: {}", ex, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APiResponse<>("Validaiton failed: " + ex.getMessage(), null, errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APiResponse<Object>> handleGeneralException(Exception ex) {
        logger.error("An unexpected error occured: {}, Message: {}", ex, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APiResponse<>("An unexpected error occured: " + ex.getMessage(), null, null));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("Error: {}, Message: {}", e, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APiResponse<>(e.getMessage(), null, null));
    }
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<APiResponse<Object>> handleRecipeDataAccesException(DataAccessException ex) {
        logger.error("Error accessing recipe data form the database: ", ex, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APiResponse<>("Error fetching recipe data from the database:",null,null));
    }
    // for innlogging
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<APiResponse<Object>> handleResponseStatusException(ResponseStatusException ex) {
        logger.error("ResponseStatusException: {}, Status: {}, Message: {}", ex, ex.getStatusCode(), ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).body(new APiResponse<>("Error: " + ex.getMessage(), null, null));
    }
}
