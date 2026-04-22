package com.example.bankapi.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal newBalance;

    @Column(nullable = false)
    private Instant timestamp;

    @Column
    private String description;

    protected Transaction() {
    }

    public Transaction(Account account, TransactionType type, BigDecimal amount, BigDecimal newBalance, String description) {
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.newBalance = newBalance;
        this.timestamp = Instant.now();
        this.description = description;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public Account getAccount() {
        return account;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public enum TransactionType {
        DEPOSIT, WITHDRAWAL
    }
}