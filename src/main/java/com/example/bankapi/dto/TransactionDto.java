package com.example.bankapi.dto;

import com.example.bankapi.model.Transaction;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class TransactionDto {

    private UUID transactionId;
    private Long accountId;
    private Transaction.TransactionType type;
    private BigDecimal amount;
    private BigDecimal newBalance;
    private Instant timestamp;
    private String description;

    public TransactionDto(UUID transactionId, Long accountId, Transaction.TransactionType type, BigDecimal amount, BigDecimal newBalance, Instant timestamp, String description) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.newBalance = newBalance;
        this.timestamp = timestamp;
        this.description = description;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Transaction.TransactionType getType() {
        return type;
    }

    public void setType(Transaction.TransactionType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(BigDecimal newBalance) {
        this.newBalance = newBalance;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}