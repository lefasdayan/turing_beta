package com.example.turing_beta.service;

import com.example.turing_beta.entity.Debt;

import java.util.List;

public interface DebtService {
    List<Debt> getAll();

    Debt add(Debt debt);

    Debt getById(Long id);

    Debt save(Debt debt);

    void deleteById(Long id);

    List<Debt> getAllByContactId(Long id);

    //todo внесение сразу нескольких долгов по одной транзакции (счет в кафе делится между несоклькими людьми.
    // на вход транзакция айди, массив контактов и массив денег, сколько каждый контакт должен, в соответствующем порядке)
}
