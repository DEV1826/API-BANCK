package com.example.bankapi.service;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
        super("Le montant doit être supérieur à 0");
    }
}
