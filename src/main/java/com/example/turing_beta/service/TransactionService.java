package com.example.turing_beta.service;

import com.example.turing_beta.entity.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> getAll();

    Transaction add(Transaction transaction);

    Transaction getById(Long id);

    Transaction save(Transaction transaction);

    Transaction delete(Transaction transaction);

    //todo обновление счета при проведении транзакции? (внесена транзакция на поступление -- на соотв. счете увеличивается amount)
    // (проблема при обновлении суммы транзакции: нужно сначала откатить старую сумму, потом обновить счет с новой
    // -- либо сразу вычислять количество денег на счету, складывая и вычитая транзакции)
}
