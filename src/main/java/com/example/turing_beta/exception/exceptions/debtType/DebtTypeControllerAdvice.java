package com.example.turing_beta.exception.exceptions.debtType;

import com.example.turing_beta.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class DebtTypeControllerAdvice {
    @ExceptionHandler(DebtTypeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDebtTypeNotFoundException(DebtTypeNotFoundException e){
        log.error(e.getMessage());
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
