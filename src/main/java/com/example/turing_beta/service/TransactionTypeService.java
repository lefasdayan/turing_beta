package com.example.turing_beta.service;

import com.example.turing_beta.entity.TransactionType;

import java.util.List;

public interface TransactionTypeService {
    List<TransactionType> getAll();

    TransactionType add(TransactionType transactionType);

    TransactionType getById(Long id);

    TransactionType save(TransactionType transactionType);

    TransactionType getByName(String name);

    void deleteById(Long id);
}
