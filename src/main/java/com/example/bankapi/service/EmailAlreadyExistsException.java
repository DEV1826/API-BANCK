package com.example.bankapi.service;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
        super("Email déjà utilisé");
    }
}