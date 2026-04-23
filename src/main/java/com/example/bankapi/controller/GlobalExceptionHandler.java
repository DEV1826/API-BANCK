package com.example.bankapi.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.bankapi.service.AccountNotFoundException;
import com.example.bankapi.service.EmailAlreadyExistsException;
import com.example.bankapi.service.InsufficientFundsException;
import com.example.bankapi.service.InvalidAmountException;

/**
 * Gestionnaire Global d'Exceptions
 * Centralise la gestion des erreurs pour l'API bancaire
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle: Compte non trouvé
     * Status: 404 Not Found
     */
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(AccountNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage());
    }

    /**
     * Handle: Validation échouée (montant invalide, format email, etc.)
     * Status: 400 Bad Request
     */
    @ExceptionHandler({InvalidAmountException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(Exception ex) {
        String message = ex instanceof InvalidAmountException 
            ? ex.getMessage() 
            : "Données invalides: vérifiez le format des champs";
        return build(HttpStatus.BAD_REQUEST, "BAD_REQUEST", message);
    }

    /**
     * Handle: Fonds insuffisants pour retrait
     * Status: 422 Unprocessable Entity
     */
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficient(InsufficientFundsException ex) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "UNPROCESSABLE_ENTITY", ex.getMessage());
    }

    /**
     * Handle: Email déjà utilisé
     * Status: 409 Conflict
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailExists(EmailAlreadyExistsException ex) {
        return build(HttpStatus.CONFLICT, "CONFLICT", ex.getMessage());
    }

    /**
     * Construit une réponse d'erreur standardisée
     */
    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}
