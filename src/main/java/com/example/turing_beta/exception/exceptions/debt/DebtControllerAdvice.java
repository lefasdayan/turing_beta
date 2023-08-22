package com.example.turing_beta.exception.exceptions.debt;

import com.example.turing_beta.exception.ErrorResponse;
import com.example.turing_beta.exception.exceptions.debtType.DebtTypeNotFoundException;
import com.example.turing_beta.exception.exceptions.transaction.TransactionAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class DebtControllerAdvice {
    @ExceptionHandler(DebtNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDebtNotFoundException(DebtNotFoundException e){
        log.error(e.getMessage());
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DebtAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleDebtAlreadyExistsException(DebtAlreadyExistsException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DebtFieldsEmptyException.class)
    public ResponseEntity<ErrorResponse> handleDebtFieldsEmptyException(DebtFieldsEmptyException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DebtWrongDueDateException.class)
    public ResponseEntity<ErrorResponse> handleDebtWrongDueDateException(DebtWrongDueDateException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
