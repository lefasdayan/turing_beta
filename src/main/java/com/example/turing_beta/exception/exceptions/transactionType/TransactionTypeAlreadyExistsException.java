package com.example.turing_beta.exception.exceptions.transactionType;

public class TransactionTypeAlreadyExistsException extends RuntimeException {
    public TransactionTypeAlreadyExistsException(String message) {
        super(message);
    }
}
