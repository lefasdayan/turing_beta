package com.example.turing_beta.exception.exceptions.transaction;

public class TransactionFieldsEmptyException extends RuntimeException {
    public TransactionFieldsEmptyException(String message) {
        super(message);
    }
}
