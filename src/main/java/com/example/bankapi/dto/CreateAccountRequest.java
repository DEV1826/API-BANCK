package com.example.bankapi.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Requête pour créer un nouveau compte bancaire")
public class CreateAccountRequest {

    @NotBlank(message = "Le nom est requis")
    @Schema(description = "Nom du propriétaire du compte", example = "JAPHET DJOMO")
    private String name;

    @NotBlank(message = "L'email est requis")
    @Email(message = "Format email invalide")
    @Schema(description = "Email unique du propriétaire", example = "JAPHETDJOM@GMAIL.COM")
    private String email;

    @NotBlank(message = "Le téléphone est requis")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Le numéro de téléphone doit contenir entre 10 et 15 chiffres, format: +[pays][numéro]")
    @Schema(description = "Numéro de téléphone au format international", example = "+237657786440")
    private String phone;

    @DecimalMin(value = "0.00", inclusive = true, message = "Le solde initial ne peut pas être négatif")
    @Schema(description = "Solde initial optionnel (par défaut: 0.00)", example = "1000.00")
    private BigDecimal initialBalance;

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

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
}
