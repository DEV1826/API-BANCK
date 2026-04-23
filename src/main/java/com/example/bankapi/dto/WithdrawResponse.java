package com.example.bankapi.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Réponse après un retrait réussi")
public class WithdrawResponse {

    @Schema(description = "Message de confirmation", example = "Retrait effectué")
    private String message;

    @Schema(description = "Identifiant du compte", example = "1")
    private Long accountId;

    @Schema(description = "Nouveau solde après le retrait", example = "900.00")
    private BigDecimal newBalance;

    public WithdrawResponse(String message, Long accountId, BigDecimal newBalance) {
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