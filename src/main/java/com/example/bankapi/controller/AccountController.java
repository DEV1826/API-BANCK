package com.example.bankapi.controller;

import com.example.bankapi.dto.*;
import com.example.bankapi.service.AccountService;
import com.example.bankapi.service.EmailAlreadyExistsException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDetails createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping
    public PagedResponse<AccountSummary> listAccounts(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int limit) {
        return accountService.listAccounts(page, limit);
    }

    @GetMapping("/{accountId}")
    public AccountDetails getAccountDetails(@PathVariable Long accountId) {
        return accountService.getAccountDetails(accountId);
    }

    @PostMapping("/{accountId}/deposit")
    public DepositResponse deposit(@PathVariable Long accountId, @Valid @RequestBody AmountRequest request) {
        return accountService.deposit(accountId, request.getAmount());
    }

    @PostMapping("/{accountId}/withdraw")
    public WithdrawResponse withdraw(@PathVariable Long accountId, @Valid @RequestBody AmountRequest request) {
        return accountService.withdraw(accountId, request.getAmount());
    }

    @GetMapping("/{accountId}/transactions")
    public List<TransactionDto> getAccountTransactions(
        @PathVariable Long accountId,
        @RequestParam(defaultValue = "20") int limit) {
        return accountService.getAccountTransactions(accountId, limit);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleEmailAlreadyExists(EmailAlreadyExistsException e) {
        return e.getMessage();
    }
}
