package com.example.bankapi.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class AccountSummary {

    private Long accountId;
    private String name;
    private String email;
    private BigDecimal balance;
    private Instant createdAt;

    public AccountSummary(Long accountId, String name, String email, BigDecimal balance, Instant createdAt) {
        this.accountId = accountId;
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}