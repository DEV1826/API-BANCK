package com.example.bankapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class AmountRequest {

    @DecimalMin(value = "0.01", inclusive = true)
    @JsonProperty("montant")
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
