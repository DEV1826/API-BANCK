package com.example.bankapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;

public class AccountResponse {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("nomTitulaire")
    private String ownerName;
    @JsonProperty("solde")
    private BigDecimal balance;
    @JsonProperty("dateCreation")
    private Instant createdAt;

    public AccountResponse(Long id, String ownerName, BigDecimal balance, Instant createdAt) {
        this.id = id;
        this.ownerName = ownerName;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
