package com.example.bankapi.service;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long id) {
        super("Compte introuvable : " + id);
    }
}
