package com.example.turing_beta.service;

import com.example.turing_beta.entity.Account;

import java.util.List;

public interface AccountService {
    List<Account> getAll();

    Account add(Account account);

    Account getById(Long id);

    Account save(Account account);

    void deleteById(Long id);

    Account getByName(String name);
}
