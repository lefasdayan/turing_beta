package com.example.turing_beta.service.impl;

import com.example.turing_beta.entity.TransactionType;
import com.example.turing_beta.exception.exceptions.transactionType.TransactionTypeAlreadyExistsException;
import com.example.turing_beta.exception.exceptions.transactionType.TransactionTypeFieldsEmptyException;
import com.example.turing_beta.exception.exceptions.transactionType.TransactionTypeNotFoundException;
import com.example.turing_beta.repos.TransactionTypeRepo;
import com.example.turing_beta.service.TransactionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionTypeServiceImpl implements TransactionTypeService {

    private final TransactionTypeRepo transactionTypeRepo;

    @Override
    public List<TransactionType> getAll() {
        return transactionTypeRepo.findAll();
    }

    @Override
    public TransactionType add(TransactionType transactionType) {
        if (transactionType.getId() != null && transactionTypeRepo.existsById(transactionType.getId())) {
            throw new TransactionTypeAlreadyExistsException(String.format("Transaction type with id %d already exists", transactionType.getId()));
        }
        if (transactionType.getName() != null && transactionTypeRepo.existsByName(transactionType.getName())) {
            throw new TransactionTypeAlreadyExistsException(String.format("Transaction type with name %s already exists", transactionType.getName()));
        }

        transactionTypeRepo.save(transactionType);
        return transactionType;
    }

    @Override
    public TransactionType getById(Long id) {
        Optional<TransactionType> foundTransactionType = transactionTypeRepo.findById(id);
        if (foundTransactionType.isEmpty()) {
            throw new TransactionTypeNotFoundException(String.format("Could not find a transaction type with id %d", id));
        }
        return foundTransactionType.get();
    }

    @Override
    public TransactionType save(TransactionType transactionType) {
        TransactionType transactionTypeFromDb = getById(transactionType.getId());

        if (!StringUtils.hasText(transactionType.getName())) {
            throw new TransactionTypeFieldsEmptyException("Transaction type name field is empty.");
        }
        transactionTypeRepo.save(transactionType);
        return transactionType;
    }

    @Override
    public TransactionType getByName(String name) {
        Optional<TransactionType> foundTransactionType = transactionTypeRepo.findByName(name);
        if (foundTransactionType.isEmpty()) {
            throw new TransactionTypeNotFoundException(String.format("Could not find a transaction type with name %s", name));
        }
        return foundTransactionType.get();
    }

    @Override
    public TransactionType delete(TransactionType transactionType) {
        if (transactionType.getId() != null && !transactionTypeRepo.existsById(transactionType.getId())) {
            throw new TransactionTypeNotFoundException(String.format("Cannot find debt with id = %d", transactionType.getId()));
        }
        transactionTypeRepo.delete(transactionType);
        return transactionType;
    }
}
