package com.example.turing_beta.service;

import com.example.turing_beta.rest.dto.account.AccountAllFields;
import com.example.turing_beta.rest.dto.account.NewAccountRequest;
import com.example.turing_beta.rest.dto.account.NewAccountResponse;
import com.example.turing_beta.rest.dto.account.UpdateAccountRequest;

import java.util.List;

public interface AccountService {
    List<AccountAllFields> getAll();

    NewAccountResponse add(NewAccountRequest request);

    AccountAllFields getById(Long id);

    AccountAllFields save(UpdateAccountRequest request);

    void deleteById(Long id);

    AccountAllFields getByName(String name);
}
