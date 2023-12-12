package com.example.turing_beta.service.impl;

import com.example.turing_beta.entity.Transaction;
import com.example.turing_beta.exception.exceptions.common.AmountSetWrongException;
import com.example.turing_beta.exception.exceptions.common.ObjectAlreadyExistsException;
import com.example.turing_beta.exception.exceptions.common.ObjectFieldsEmptyException;
import com.example.turing_beta.exception.exceptions.common.ObjectNotFoundException;
import com.example.turing_beta.exception.exceptions.transaction.TransactionCredentialsSetWrongException;
import com.example.turing_beta.repos.TransactionRepo;
import com.example.turing_beta.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepo transactionRepo;

    @Override
    public List<Transaction> getAll() {
        return transactionRepo.findAll();
    }

    @Override
    public Transaction add(Transaction transaction) {
        checkFieldsValidity(transaction);

        if (transaction.getFromAcc() != null) {
            transaction.getFromAcc().setAmount(transaction.getFromAcc().getAmount().subtract(transaction.getAmount()));
        }

        if (transaction.getToAcc() != null) {
            transaction.getToAcc().setAmount(transaction.getToAcc().getAmount().add(transaction.getAmount()));
        }

        transaction = transactionRepo.save(transaction);
        return transaction;
    }

    @Override
    public Transaction getById(Long id) {
        Optional<Transaction> foundTransaction = transactionRepo.findById(id);
        if (foundTransaction.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Could not find a transaction with name %d", id));
        }
        return foundTransaction.get();
    }

    @Override
    public Transaction save(Transaction transaction) {
        Transaction transactionFromDb = getById(transaction.getId());

        checkFieldsValidity(transaction);

        if (transaction.getFromAcc() != null) {
            transaction.getFromAcc().setAmount(transaction.getFromAcc().getAmount().add(transactionFromDb.getAmount()).subtract(transaction.getAmount()));
        }

        if (transaction.getToAcc() != null) {
            transaction.getToAcc().setAmount(transaction.getToAcc().getAmount().subtract(transactionFromDb.getAmount()).add(transaction.getAmount()));
        }

        transaction = transactionRepo.save(transaction);
        return transaction;
    }

    @Override
    public void deleteById(Long id) {
        Transaction transactionFromDb = getById(id);

        if (transactionFromDb.getFromAcc() != null) {
            transactionFromDb.getFromAcc().setAmount(transactionFromDb.getFromAcc().getAmount().add(transactionFromDb.getAmount()));
        }

        if (transactionFromDb.getToAcc() != null) {
            transactionFromDb.getToAcc().setAmount(transactionFromDb.getToAcc().getAmount().subtract(transactionFromDb.getAmount()));
        }

        transactionRepo.deleteById(id);
    }

    private void checkFieldsValidity(Transaction transaction)
            throws ObjectAlreadyExistsException, ObjectFieldsEmptyException,
            TransactionCredentialsSetWrongException, AmountSetWrongException {
        if (transaction.getId() != null && transactionRepo.existsById(transaction.getId())) {
            throw new ObjectAlreadyExistsException(String.format("Transaction with id = %d already exists", transaction.getId()));
        }
        if (!StringUtils.hasText(transaction.getName())
                || transaction.getAmount() == null
                || transaction.getCurrency() == null) {
            throw new ObjectFieldsEmptyException("Cannot add transaction with empty field(s)");
        }
        if (transaction.getFromAcc() == null && transaction.getToAcc() == null) {
            throw new TransactionCredentialsSetWrongException("Cannot add transaction without any account assigned");
        }
        if (transaction.getFromAcc().equals(transaction.getToAcc())) {
            throw new TransactionCredentialsSetWrongException("Cannot transfer money from and to the same account");
        }
        if (transaction.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new AmountSetWrongException("Cannot add transaction with amount less than or equal to 0");
        }
        if (transactionRepo.findByName(transaction.getName()).isPresent()
                && !Objects.equals(transactionRepo.findByName(transaction.getName()).get().getId(), transaction.getId())) {
            throw new ObjectAlreadyExistsException(String.format("Cannot save. " +
                    "Transaction with name %s already exists", transaction.getName()));
        }
    }
}
