package org.example.roomrelish.ExceptionHandler;

import com.mongodb.MongoSocketException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleCustomDuplicateException(CustomDuplicateBookingException duplicateKeyException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("The payment details already exist in this id");
    }

    @ExceptionHandler
    public ResponseEntity<?> handleCustomDataAccessException(CustomDataAccessException customDataAccessException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Error occurred while accessing database");
    }

    @ExceptionHandler
    public ResponseEntity<?> handleMongoSocketException(CustomMongoSocketException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("The Server is currently unavailable");
    }
}

