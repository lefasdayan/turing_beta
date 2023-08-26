package com.example.turing_beta.exception.exceptions.common;

import com.example.turing_beta.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CommonControllerAdvice {
    @ExceptionHandler(AmountSetWrongException.class)
    public ResponseEntity<ErrorResponse> handleAmountSetWrongException(AmountSetWrongException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityAlreadyExistsException(ObjectAlreadyExistsException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ObjectFieldsEmptyException.class)
    public ResponseEntity<ErrorResponse> handleEntityFieldsEmptyException(ObjectFieldsEmptyException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleObjectNotFoundException(ObjectNotFoundException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
