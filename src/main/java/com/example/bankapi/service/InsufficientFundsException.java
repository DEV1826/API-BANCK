package com.example.bankapi.service;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Solde insuffisant");
    }
}
