package com.example.bankapi.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;

@Schema(description = "Requête pour un montant de dépôt ou retrait")
public class AmountRequest {

    @DecimalMin(value = "0.01", inclusive = true, message = "Le montant doit être supérieur à 0.00")
    @JsonProperty("montant")
    @Schema(description = "Montant à déposer ou retirer (minimum 0.01)", example = "100.00")
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
