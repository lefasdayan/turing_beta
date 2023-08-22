package com.example.turing_beta.rest;

import com.example.turing_beta.entity.Transaction;
import com.example.turing_beta.service.impl.TransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionServiceImpl transactionService;

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(){
        return ResponseEntity.ok(transactionService.getAll());
    }

    @PostMapping
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction){
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.add(transaction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id){
        return ResponseEntity.ok(transactionService.getById(id));
    }

    @PatchMapping
    public ResponseEntity<Transaction> updateTransaction(@RequestBody Transaction transaction){
        return ResponseEntity.ok(transactionService.save(transaction));
    }

    @DeleteMapping
    public ResponseEntity<Transaction> deleteTransaction(@RequestBody Transaction transaction){
        return ResponseEntity.ok(transactionService.delete(transaction));
    }
}
