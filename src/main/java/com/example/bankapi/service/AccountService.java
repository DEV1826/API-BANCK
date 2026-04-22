package com.example.bankapi.service;

import com.example.bankapi.dto.*;
import com.example.bankapi.model.Account;
import com.example.bankapi.model.Transaction;
import com.example.bankapi.repository.AccountRepository;
import com.example.bankapi.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Account createAccount(CreateAccountRequest request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
        BigDecimal balance = request.getInitialBalance() == null ? BigDecimal.ZERO : request.getInitialBalance();
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException();
        }
        Account account = new Account(request.getName(), request.getEmail(), request.getPhone(), balance);
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public List<AccountSummary> listAllAccounts() {
        return accountRepository.findAll().stream()
            .map(this::toSummary)
            .toList();
    }

    @Transactional(readOnly = true)
    public Account getAccountDetails(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        return toAccount(account);
    }

    @Transactional
    public DepositResponse deposit(Long id, BigDecimal amount) {
        validateAmount(amount);
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);

        Transaction transaction = new Transaction(account, Transaction.TransactionType.DEPOSIT, amount, newBalance, "Dépôt espèces");
        transactionRepository.save(transaction);

        return new DepositResponse("Dépôt effectué", UUID.randomUUID(), newBalance); // Note: accountId should be UUID, but model uses Long
    }

    @Transactional
    public WithdrawResponse withdraw(Long id, BigDecimal amount) {
        validateAmount(amount);
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);

        Transaction transaction = new Transaction(account, Transaction.TransactionType.WITHDRAWAL, amount, newBalance, "Retrait espèces");
        transactionRepository.save(transaction);

        return new WithdrawResponse("Retrait effectué", UUID.randomUUID(), newBalance);
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> getAccountTransactions(Long accountId, int limit) {
        List<Transaction> transactions = transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);
        return transactions.stream()
            .limit(limit)
            .map(this::toTransactionDto)
            .toList();
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }
    }

    private AccountSummary toSummary(Account account) {
        return new AccountSummary(UUID.randomUUID(), account.getOwnerName(), account.getEmail(), account.getBalance(), account.getCreatedAt());
    }

    private Account toAccount(Account account) {
        return new Account(UUID.randomUUID(), account.getOwnerName(), account.getEmail(), account.getPhone(), account.getBalance(), account.getCreatedAt(), account.getUpdatedAt());
    }

    private TransactionDto toTransactionDto(Transaction transaction) {
        return new TransactionDto(
            transaction.getTransactionId(),
            UUID.randomUUID(), // accountId
            transaction.getType(),
            transaction.getAmount(),
            transaction.getNewBalance(),
            transaction.getTimestamp(),
            transaction.getDescription()
        );
    }
}
