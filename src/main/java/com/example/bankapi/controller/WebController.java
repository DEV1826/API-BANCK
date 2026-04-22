package com.example.bankapi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.bankapi.dto.AccountResponse;
import com.example.bankapi.dto.CreateAccountRequest;
import com.example.bankapi.service.AccountService;

import jakarta.validation.Valid;

@Controller
public class WebController {

    private final AccountService accountService;

    public WebController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/swagger-ui/index.html";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/accounts")
    public String accounts(Model model) {
        if (!model.containsAttribute("createRequest")) {
            model.addAttribute("createRequest", new CreateAccountRequest());
        }
        List<AccountResponse> accounts = accountService.listAccounts();
        model.addAttribute("accounts", accounts);
        return "accounts";
    }

    @PostMapping("/accounts/create")
    public String createAccount(@Valid @ModelAttribute("createRequest") CreateAccountRequest request,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("accounts", accountService.listAccounts());
            return "accounts";
        }
        accountService.createAccount(request.getOwnerName(), request.getInitialBalance());
        return "redirect:/accounts";
    }
}