package com.example.turing_beta.exception.exceptions.contact;

public class ContactNotFoundException extends RuntimeException{
    public ContactNotFoundException(String message) {
        super(message);
    }
}
