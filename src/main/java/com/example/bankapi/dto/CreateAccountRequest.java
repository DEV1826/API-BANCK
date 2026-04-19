package com.example.bankapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class CreateAccountRequest {

    @NotBlank
    @JsonProperty("nomTitulaire")
    private String ownerName;

    @DecimalMin(value = "0.00", inclusive = true)
    @JsonProperty("soldeInitial")
    private BigDecimal initialBalance;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
}
