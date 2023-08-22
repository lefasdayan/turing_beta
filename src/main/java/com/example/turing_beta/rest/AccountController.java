package com.example.turing_beta.rest;

import com.example.turing_beta.entity.Account;
import com.example.turing_beta.service.impl.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountServiceImpl accountService;

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts(){
        return ResponseEntity.ok(accountService.getAll());
    }

    @PostMapping
    public ResponseEntity<Account> addAccount(@RequestBody Account account){
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.add(account));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id){
        return ResponseEntity.ok(accountService.getById(id));
    }

    @PatchMapping // todo Переместить метод в сваггере на нужный адрес, поменять название и реквест
    public ResponseEntity<Account> updateAccount(@RequestBody Account account){
        return ResponseEntity.ok(accountService.save(account));
    }

    @DeleteMapping// todo Переместить метод в сваггере на нужный адрес, поменять название и реквест
    public ResponseEntity<Account> deleteAccount(@RequestBody Account account){
        return ResponseEntity.ok(accountService.delete(account));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Account> getAccountByName(@PathVariable String name){
        return ResponseEntity.ok(accountService.getByName(name));
    }
}
