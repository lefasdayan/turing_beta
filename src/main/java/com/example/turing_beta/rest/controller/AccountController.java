package com.example.turing_beta.rest.controller;

import com.example.turing_beta.entity.Account;
import com.example.turing_beta.rest.dto.account.AccountAllFields;
import com.example.turing_beta.rest.dto.account.NewAccountRequest;
import com.example.turing_beta.rest.dto.account.NewAccountResponse;
import com.example.turing_beta.rest.dto.account.UpdateAccountRequest;
import com.example.turing_beta.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountAllFields>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAll());
    }

    @PostMapping
    public ResponseEntity<NewAccountResponse> addAccount(@RequestBody NewAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.add(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountAllFields> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getById(id));
    }

    @PatchMapping
    public ResponseEntity<AccountAllFields> updateAccount(@RequestBody UpdateAccountRequest request) {
        return ResponseEntity.ok(accountService.save(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        accountService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<AccountAllFields> getAccountByName(@PathVariable String name) {
        return ResponseEntity.ok(accountService.getByName(name));
    }
}
