package com.example.bankapi.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class AccountDetails {

    private Long accountId;
    private String name;
    private String email;
    private String phone;
    private BigDecimal balance;
    private Instant createdAt;
    private Instant updatedAt;

    public AccountDetails(Long accountId, String name, String email, String phone, BigDecimal balance, Instant createdAt, Instant updatedAt) {
        this.accountId = accountId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.balance = balance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}