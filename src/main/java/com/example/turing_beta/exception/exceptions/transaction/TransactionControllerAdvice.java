package com.example.turing_beta.exception.exceptions.transaction;

import com.example.turing_beta.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class TransactionControllerAdvice {
    @ExceptionHandler(TransactionCredentialsSetWrongException.class)
    public ResponseEntity<ErrorResponse> handleTransactionCredentialsSetWrongException(TransactionCredentialsSetWrongException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
