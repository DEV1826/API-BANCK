package com.example.bankapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankapi.dto.AccountDetails;
import com.example.bankapi.dto.AccountSummary;
import com.example.bankapi.dto.AmountRequest;
import com.example.bankapi.dto.CreateAccountRequest;
import com.example.bankapi.dto.DepositResponse;
import com.example.bankapi.dto.ErrorResponse;
import com.example.bankapi.dto.PagedResponse;
import com.example.bankapi.dto.TransactionDto;
import com.example.bankapi.dto.WithdrawResponse;
import com.example.bankapi.service.AccountNotFoundException;
import com.example.bankapi.service.AccountService;
import com.example.bankapi.service.EmailAlreadyExistsException;
import com.example.bankapi.service.InsufficientFundsException;
import com.example.bankapi.service.InvalidAmountException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Comptes Bancaires", description = "API pour la gestion des comptes bancaires - Création, consultation, dépôts et retraits")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "F1: Créer un nouveau compte bancaire",
        description = "Crée un compte bancaire avec validation unique de l'email, format téléphone et montant initial optionnel."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Compte créé avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDetails.class))),
        @ApiResponse(responseCode = "400", description = "Données invalides (email, téléphone, montant négatif)"),
        @ApiResponse(responseCode = "409", description = "Email déjà utilisé par un autre compte")
    })
    public AccountDetails createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping
    @Operation(
        summary = "F2: Lister tous les comptes (paginé)",
        description = "Récupère la liste des comptes avec pagination. Page commence à 1."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des comptes",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedResponse.class))),
        @ApiResponse(responseCode = "400", description = "Paramètres de pagination invalides")
    })
    public PagedResponse<AccountSummary> listAccounts(
        @Parameter(description = "Numéro de page (commence à 1)", example = "1")
        @RequestParam(defaultValue = "1") int page,
        @Parameter(description = "Nombre de résultats par page", example = "10")
        @RequestParam(defaultValue = "10") int limit) {
        return accountService.listAccounts(page, limit);
    }

    @GetMapping("/{accountId}")
    @Operation(
        summary = "F3: Récupérer les détails d'un compte",
        description = "Obtient toutes les informations détaillées d'un compte spécifique avec son solde actuel."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Détails du compte",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDetails.class))),
        @ApiResponse(responseCode = "404", description = "Compte non trouvé")
    })
    public AccountDetails getAccountDetails(
        @Parameter(description = "Identifiant unique du compte", example = "1")
        @PathVariable Long accountId) {
        return accountService.getAccountDetails(accountId);
    }

    @PostMapping("/{accountId}/deposit")
    @Operation(
        summary = "F4: Effectuer un dépôt",
        description = "Ajoute un montant au solde du compte. La transaction est enregistrée dans l'historique."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dépôt effectué avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DepositResponse.class))),
        @ApiResponse(responseCode = "400", description = "Montant invalide (≤ 0)"),
        @ApiResponse(responseCode = "404", description = "Compte non trouvé")
    })
    public DepositResponse deposit(
        @Parameter(description = "Identifiant du compte", example = "1")
        @PathVariable Long accountId,
        @Valid @RequestBody AmountRequest request) {
        return accountService.deposit(accountId, request.getAmount());
    }

    @PostMapping("/{accountId}/withdraw")
    @Operation(
        summary = "F5: Effectuer un retrait",
        description = "Retire un montant du solde du compte. Vérifie les fonds suffisants. La transaction est enregistrée."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retrait effectué avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = WithdrawResponse.class))),
        @ApiResponse(responseCode = "400", description = "Montant invalide (≤ 0)"),
        @ApiResponse(responseCode = "404", description = "Compte non trouvé"),
        @ApiResponse(responseCode = "422", description = "Solde insuffisant pour effectuer le retrait")
    })
    public WithdrawResponse withdraw(
        @Parameter(description = "Identifiant du compte", example = "1")
        @PathVariable Long accountId,
        @Valid @RequestBody AmountRequest request) {
        return accountService.withdraw(accountId, request.getAmount());
    }

    @GetMapping("/{accountId}/transactions")
    @Operation(
        summary = "F6: Consulter l'historique des transactions",
        description = "Récupère la liste des transactions du compte, triées par date décroissante (plus récentes en premier)."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historique des transactions",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
        @ApiResponse(responseCode = "404", description = "Compte non trouvé")
    })
    public List<TransactionDto> getAccountTransactions(
        @Parameter(description = "Identifiant du compte", example = "1")
        @PathVariable Long accountId,
        @Parameter(description = "Nombre maximum de transactions à retourner", example = "20")
        @RequestParam(defaultValue = "20") int limit) {
        return accountService.getAccountTransactions(accountId, limit);
    }

    // Gestionnaires d'exceptions

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailAlreadyExists(EmailAlreadyExistsException e) {
        return new ErrorResponse("CONFLICT", e.getMessage(), 409);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAccountNotFound(AccountNotFoundException e) {
        return new ErrorResponse("NOT_FOUND", e.getMessage(), 404);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleInsufficientFunds(InsufficientFundsException e) {
        return new ErrorResponse("UNPROCESSABLE_ENTITY", e.getMessage(), 422);
    }

    @ExceptionHandler(InvalidAmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidAmount(InvalidAmountException e) {
        return new ErrorResponse("BAD_REQUEST", e.getMessage(), 400);
    }
}
