package com.example.bankapi.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Réponse après un dépôt réussi")
public class DepositResponse {

    @Schema(description = "Message de confirmation", example = "Dépôt effectué")
    private String message;

    @Schema(description = "Identifiant du compte", example = "1")
    private Long accountId;

    @Schema(description = "Nouveau solde après le dépôt", example = "1500.00")
    private BigDecimal newBalance;

    public DepositResponse(String message, Long accountId, BigDecimal newBalance) {
        this.message = message;
        this.accountId = accountId;
        this.newBalance = newBalance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(BigDecimal newBalance) {
        this.newBalance = newBalance;
    }
}