package com.example.bankapi.dto;

import java.math.BigDecimal;

public class WithdrawResponse {

    private String message;
    private Long accountId;
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