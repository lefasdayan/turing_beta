package com.example.turing_beta.service.impl;

import com.example.turing_beta.entity.TransactionType;
import com.example.turing_beta.exception.exceptions.common.ObjectAlreadyExistsException;
import com.example.turing_beta.exception.exceptions.common.ObjectFieldsEmptyException;
import com.example.turing_beta.exception.exceptions.common.ObjectNotFoundException;
import com.example.turing_beta.repos.TransactionTypeRepo;
import com.example.turing_beta.service.TransactionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
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
            throw new ObjectAlreadyExistsException(String.format("Transaction type with id %d already exists", transactionType.getId()));
        }
        if (transactionType.getName() != null && transactionTypeRepo.existsByName(transactionType.getName())) {
            throw new ObjectAlreadyExistsException(String.format("Transaction type with name %s already exists", transactionType.getName()));
        }

        transactionType = transactionTypeRepo.save(transactionType);
        return transactionType;
    }

    @Override
    public TransactionType getById(Long id) {
        Optional<TransactionType> foundTransactionType = transactionTypeRepo.findById(id);
        if (foundTransactionType.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Could not find a transaction type with id %d", id));
        }
        return foundTransactionType.get();
    }

    @Override
    public TransactionType save(TransactionType transactionType) {
        TransactionType transactionTypeFromDb = getById(transactionType.getId());

        if (!StringUtils.hasText(transactionType.getName())) {
            throw new ObjectFieldsEmptyException("Transaction type name field is empty.");
        }
        if (transactionTypeRepo.findByName(transactionType.getName()).isPresent()
                && !Objects.equals(transactionTypeRepo.findByName(transactionType.getName()).get().getId(), transactionType.getId())){ //todo перенести в метод checkFields
            throw new ObjectAlreadyExistsException(String.format("Cannot save. " +
                    "Transaction type with name %s already exists", transactionType.getName()));
        }
        transactionType = transactionTypeRepo.save(transactionType);
        return transactionType;
    }

    @Override
    public TransactionType getByName(String name) {
        Optional<TransactionType> foundTransactionType = transactionTypeRepo.findByName(name);
        if (foundTransactionType.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Could not find a transaction type with name %s", name));
        }
        return foundTransactionType.get();
    }

    @Override
    public void deleteById(Long id) {
        TransactionType transactionType = getById(id);
        transactionTypeRepo.deleteById(id);
    }
}
