package com.example.turing_beta.service.impl;

import com.example.turing_beta.entity.Account;
import com.example.turing_beta.exception.exceptions.common.AmountSetWrongException;
import com.example.turing_beta.exception.exceptions.common.ObjectAlreadyExistsException;
import com.example.turing_beta.exception.exceptions.common.ObjectFieldsEmptyException;
import com.example.turing_beta.exception.exceptions.common.ObjectNotFoundException;
import com.example.turing_beta.repos.AccountRepo;
import com.example.turing_beta.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepo accountRepo;

    @Override
    public List<Account> getAll() {
        return accountRepo.findAll();
    }

    @Override
    public Account add(Account account) {
        if (account.getId() != null && accountRepo.existsById(account.getId())) {
            throw new ObjectAlreadyExistsException(String.format("Account with id = %d already exists", account.getId()));
        }
        checkFieldsValidity(account);

        account = accountRepo.save(account);
        return account;
    }

    @Override
    public Account getById(Long id) {
        Optional<Account> foundAcc = accountRepo.findById(id);
        if (foundAcc.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Cannot find account with id = %d", id));
        }
        return foundAcc.get();
    }

    @Override
    public Account save(Account account) {
        Account accFromDb = getById(account.getId());

        checkFieldsValidity(account);

        account = accountRepo.save(account);
        return account;
    }

    @Override
    public void deleteById(Long id) {
        Account account = getById(id);
        accountRepo.deleteById(id);
    }

    @Override
    public Account getByName(String name) {
        Optional<Account> foundAccount = accountRepo.findByName(name);
        if (foundAccount.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Cannot find account with name %s", name));
        }
        return foundAccount.get();
    }

    private void checkFieldsValidity(Account account) throws ObjectFieldsEmptyException, AmountSetWrongException {
        if (!StringUtils.hasText(account.getName())
                || account.getAmount() == null
                || account.getCurrency() == null) {
            throw new ObjectFieldsEmptyException("Cannot save account with empty field(s)");
        }
        if (account.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new AmountSetWrongException("Cannot save account with amount bigger than or equal to 0");
        }
    }
}
