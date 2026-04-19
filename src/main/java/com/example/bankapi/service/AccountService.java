package com.example.bankapi.service;

import com.example.bankapi.dto.AccountResponse;
import com.example.bankapi.model.Account;
import com.example.bankapi.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccountResponse createAccount(String ownerName, BigDecimal initialBalance) {
        BigDecimal balance = initialBalance == null ? BigDecimal.ZERO : initialBalance;
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException();
        }
        Account account = new Account(ownerName, balance);
        Account saved = accountRepository.save(account);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> listAccounts() {
        return accountRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public AccountResponse deposit(Long id, BigDecimal amount) {
        validateAmount(amount);
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        account.setBalance(account.getBalance().add(amount));
        return toResponse(account);
    }

    @Transactional
    public AccountResponse withdraw(Long id, BigDecimal amount) {
        validateAmount(amount);
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
        account.setBalance(account.getBalance().subtract(amount));
        return toResponse(account);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }
    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(account.getId(), account.getOwnerName(), account.getBalance(), account.getCreatedAt());
    }
}
