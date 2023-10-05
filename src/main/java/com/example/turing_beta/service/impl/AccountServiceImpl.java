package com.example.turing_beta.service.impl;

import com.example.turing_beta.entity.Account;
import com.example.turing_beta.exception.exceptions.account.AccountCurrencyChangingException;
import com.example.turing_beta.exception.exceptions.common.AmountSetWrongException;
import com.example.turing_beta.exception.exceptions.common.ObjectAlreadyExistsException;
import com.example.turing_beta.exception.exceptions.common.ObjectFieldsEmptyException;
import com.example.turing_beta.exception.exceptions.common.ObjectNotFoundException;
import com.example.turing_beta.repos.AccountRepo;
import com.example.turing_beta.rest.dto.account.AccountAllFields;
import com.example.turing_beta.rest.dto.account.NewAccountRequest;
import com.example.turing_beta.rest.dto.account.NewAccountResponse;
import com.example.turing_beta.rest.dto.account.UpdateAccountRequest;
import com.example.turing_beta.service.AccountService;
import com.example.turing_beta.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepo accountRepo;
    private final CurrencyService currencyService;

    @Override
    public List<AccountAllFields> getAll() {
        return accountRepo.findAll().stream()
                .map(this::transformToAllFieldsDto)
                .collect(Collectors.toList());
    }

    @Override
    public NewAccountResponse add(NewAccountRequest request) {
        if (request.getCurrencyId() == null) {
            throw new ObjectFieldsEmptyException("Cannot save account with empty field(s)");
        }
        Account account = Account.builder()
                .name(request.getName())
                .amount(request.getAmount())
                .currency(currencyService.getById(request.getCurrencyId()))
                .build();

        checkFieldsValidity(account);
        account = accountRepo.save(account);

        return NewAccountResponse.builder()
                .id(account.getId())
                .name(account.getName())
                .amount(account.getAmount())
                .currencyName(account.getCurrency().getName())
                .build();
    }

    @Override
    public AccountAllFields getById(Long id) {
        Optional<Account> foundAcc = accountRepo.findById(id);
        if (foundAcc.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Cannot find account with id = %d", id));
        }
        Account account = foundAcc.get();
        return transformToAllFieldsDto(account);
    }

    @Override
    public AccountAllFields save(UpdateAccountRequest request) {
        AccountAllFields accFromDb = getById(request.getId());

        if (request.getCurrencyId() == null) {
            throw new ObjectFieldsEmptyException("Cannot save account with empty field(s)");
        }
        if (!request.getCurrencyId().equals(currencyService.getByName(accFromDb.getCurrencyName()).getId())){
            throw new AccountCurrencyChangingException("Cannot change currency of an account after it's creation");
        }

        Account account = Account.builder()
                .id(request.getId())
                .name(request.getName())
                .amount(request.getAmount())
                .currency(currencyService.getById(request.getCurrencyId()))
                .bankName(request.getBankName())
                .build();

        checkFieldsValidity(account);

        account = accountRepo.save(account);
        return transformToAllFieldsDto(account);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        accountRepo.deleteById(id);
    }

    @Override
    public AccountAllFields getByName(String name) {
        Optional<Account> foundAccount = accountRepo.findByName(name);
        if (foundAccount.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Cannot find account with name %s", name));
        }
        Account account = foundAccount.get();
        return transformToAllFieldsDto(account);
    }

    private void checkFieldsValidity(Account account)
            throws ObjectFieldsEmptyException, AmountSetWrongException, ObjectAlreadyExistsException {
        if (!StringUtils.hasText(account.getName())
                || account.getAmount() == null) {
            throw new ObjectFieldsEmptyException("Cannot save account with empty field(s)");
        }
        if (account.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new AmountSetWrongException("Cannot save account with amount less than or equal to 0");
        }
        //если аккаунт с таким именем уже существует, и у него отличный айдишник, то выкинуть эксепшн
        if (accountRepo.findByName(account.getName()).isPresent()
                && !Objects.equals(accountRepo.findByName(account.getName()).get().getId(), account.getId())){
            throw new ObjectAlreadyExistsException(String.format("Cannot save. " +
                    "Account with name \"%s\" already exists", account.getName()));
        }
    }

    private AccountAllFields transformToAllFieldsDto(Account account){
        return AccountAllFields.builder()
                .id(account.getId())
                .name(account.getName())
                .amount(account.getAmount())
                .currencyName(account.getCurrency().getName())
                .bankName(account.getBankName())
                .build();
    }
}