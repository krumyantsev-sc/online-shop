package com.scand.bookshop.exception;

import com.scand.bookshop.utility.ServerMessage;
import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final String PROPERTY_VALUE_EXCEPTION_MESSAGE = "Field values are not valid";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(PropertyValueException.class)
    public ResponseEntity<String> propertyValueException(PropertyValueException e) {
        return ResponseEntity.badRequest().body(PROPERTY_VALUE_EXCEPTION_MESSAGE);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ServerMessage> UserAlreadyExistsException(PropertyValueException e) {
        return ResponseEntity.badRequest().body(new ServerMessage("error",e.getMessage()));
    }
}