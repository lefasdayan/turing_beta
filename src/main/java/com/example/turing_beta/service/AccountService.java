package com.example.turing_beta.service;

import com.example.turing_beta.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> getAll();

    Account add(Account account);

    Account getById(Long id);

    Account save(Account account);

    Account delete(Account account);

    Account getByName(String name);
}
