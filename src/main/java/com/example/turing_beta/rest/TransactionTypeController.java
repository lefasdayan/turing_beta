package com.example.turing_beta.rest;

import com.example.turing_beta.entity.TransactionType;
import com.example.turing_beta.service.TransactionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction_type")
@RequiredArgsConstructor
public class TransactionTypeController {
    private final TransactionTypeService transactionTypeService;

    @GetMapping
    public ResponseEntity<List<TransactionType>> getAllTransactionTypes(){
        return ResponseEntity.ok(transactionTypeService.getAll());
    }

    @PostMapping
    public ResponseEntity<TransactionType> addTransactionType(@RequestBody TransactionType transactionType){
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionTypeService.add(transactionType));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionType> getTransactionTypeById(@PathVariable Long id){
        return ResponseEntity.ok(transactionTypeService.getById(id));
    }

    @PatchMapping
    public ResponseEntity<TransactionType> updateTransactionType(@RequestBody TransactionType transactionType){
        return ResponseEntity.ok(transactionTypeService.save(transactionType));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<TransactionType> getTransactionTypeByName(@PathVariable String name){
        return ResponseEntity.ok(transactionTypeService.getByName(name));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransactionType(@PathVariable Long id){
        transactionTypeService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
