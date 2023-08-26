package com.example.turing_beta.service.impl;

import com.example.turing_beta.entity.Account;
import com.example.turing_beta.entity.Currency;
import com.example.turing_beta.exception.exceptions.common.AmountSetWrongException;
import com.example.turing_beta.exception.exceptions.common.ObjectFieldsEmptyException;
import com.example.turing_beta.repos.AccountRepo;
import com.example.turing_beta.service.AccountService;
import com.example.turing_beta.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AccountServiceImplTest {
    @Autowired
    private AccountService accountService;

    @Autowired
    private CurrencyService currencyService;

    @MockBean
    private AccountRepo accountRepo;

    @Test
    void getAll() {
        Currency currency = currencyService.getById(1L);

        Account acc1 = new Account();
        acc1.setId(1L);
        acc1.setAmount(BigDecimal.valueOf(2));
        acc1.setName("Test acc 1");
        acc1.setCurrency(currency);
        acc1.setBankName("Sber");

        Account acc2 = new Account();
        acc2.setId(2L);
        acc2.setAmount(BigDecimal.valueOf(300));
        acc2.setName("Test acc 2");
        acc2.setCurrency(currency);
        acc2.setBankName("Tinkoff");

        List<Account> accounts = new ArrayList<>(Arrays.asList(acc1, acc2));

        when(accountService.getAll()).thenReturn(accounts);

        List<Account> accountsFromDb = accountService.getAll();

        assertNotEquals(accountsFromDb.size(), 0);

        for (Account account : accountsFromDb) {
            assertNotEquals(account, null);
        }

        verify(accountRepo, times(1)).findAll();
    }

    @Test
    void addSuccessfully() {
        Currency currency = currencyService.getById(1L);

        Account account = new Account();
        account.setAmount(BigDecimal.valueOf(2));
        account.setName("Test acc");
        account.setCurrency(currency);
        account.setBankName("Sber");

        Account accFromDb = new Account();
        accFromDb.setAmount(BigDecimal.valueOf(2));
        accFromDb.setName("Test acc");
        accFromDb.setCurrency(currency);
        accFromDb.setBankName("Sber");
        accFromDb.setId(1L);

        doReturn(accFromDb)
                .when(accountRepo)
                .save(account);

        Account savedAcc = accountService.add(account);

        assertEquals(savedAcc.getId(), 1L);
        assertEquals(savedAcc.getBankName(), "Sber");
        assertEquals(savedAcc.getCurrency(), currency);
        assertEquals(savedAcc.getName(), "Test acc");
        assertEquals(savedAcc.getAmount(), BigDecimal.valueOf(2));

        verify(accountRepo, times(1)).save(any(Account.class));
    }

    @Test
    void addExceptionFieldsEmpty() {
        Currency currency = currencyService.getById(1L);

        Account account = new Account();
        account.setAmount(BigDecimal.valueOf(2));
        account.setName(null);
        account.setCurrency(currency);
        account.setBankName("Sber");
        assertThatExceptionOfType(ObjectFieldsEmptyException.class)
                .isThrownBy(() -> accountService.add(account))
                .withMessage("Cannot save account with empty field(s)");

        account.setName("Test acc");
        account.setAmount(null);
        assertThatExceptionOfType(ObjectFieldsEmptyException.class)
                .isThrownBy(() -> accountService.add(account))
                .withMessage("Cannot save account with empty field(s)");

        account.setAmount(BigDecimal.valueOf(2));
        account.setCurrency(null);
        assertThatExceptionOfType(ObjectFieldsEmptyException.class)
                .isThrownBy(() -> accountService.add(account))
                .withMessage("Cannot save account with empty field(s)");
    }

    @Test
    void addWithWrongAmount() {
        Currency currency = currencyService.getById(1L);

        Account account = new Account();
        account.setName("Test acc");
        account.setCurrency(currency);
        account.setAmount(BigDecimal.valueOf(-2));
        account.setBankName("Sber");

        assertThatExceptionOfType(AmountSetWrongException.class)
                .isThrownBy(() -> accountService.add(account))
                .withMessage("Cannot save account with amount bigger than or equal to 0");
    }

    @Test
    void getById() {
        Currency currency = currencyService.getById(1L);

        Account account = new Account();
        account.setId(1L);
        account.setAmount(BigDecimal.valueOf(2));
        account.setName("Test acc");
        account.setCurrency(currency);
        account.setBankName("Sber");

        doReturn(Optional.of(account))
                .when(accountRepo)
                .findById(1L);

        Account accFromDb = accountService.getById(1L);
        assertNotEquals(accFromDb, null);
        assertEquals(accFromDb.getId(), 1L);
        assertEquals(accFromDb.getBankName(), "Sber");
        assertEquals(accFromDb.getCurrency(), currency);
        assertEquals(accFromDb.getName(), "Test acc");
        assertEquals(accFromDb.getAmount(), BigDecimal.valueOf(2));

        verify(accountRepo, times(1)).findById(any(Long.class));
    }

    @Test
    void saveSuccessfully() {
        Currency currency = currencyService.getById(1L);

        Account account = new Account();
        account.setAmount(BigDecimal.valueOf(2));
        account.setName("Test acc");
        account.setCurrency(currency);
        account.setBankName("Sber");
        account.setId(1L);

        Account accUpdated = new Account();
        accUpdated.setAmount(BigDecimal.valueOf(500));
        accUpdated.setName("Test acc");
        accUpdated.setCurrency(currency);
        accUpdated.setBankName("Sber");
        accUpdated.setId(1L);

        doReturn(Optional.of(account))
                .when(accountRepo)
                .findById(1L);

        doReturn(accUpdated)
                .when(accountRepo)
                .save(account);

        Account savedAcc = accountService.save(account);

        assertEquals(savedAcc.getId(), 1L);
        assertEquals(savedAcc.getBankName(), "Sber");
        assertEquals(savedAcc.getCurrency(), currency);
        assertEquals(savedAcc.getName(), "Test acc");
        assertEquals(savedAcc.getAmount(), BigDecimal.valueOf(500));

        verify(accountRepo, times(1)).save(any(Account.class));
    }

    @Test
    void saveExceptionFieldsEmpty() {
        Currency currency = currencyService.getById(1L);

        Account account = new Account();
        account.setAmount(BigDecimal.valueOf(2));
        account.setName(null);
        account.setCurrency(currency);
        account.setBankName("Sber");
        account.setId(1L);

        doReturn(Optional.of(account))
                .when(accountRepo)
                .findById(1L);

        assertThatExceptionOfType(ObjectFieldsEmptyException.class)
                .isThrownBy(() -> accountService.save(account))
                .withMessage("Cannot save account with empty field(s)");

        account.setName("Test acc");
        account.setAmount(null);
        assertThatExceptionOfType(ObjectFieldsEmptyException.class)
                .isThrownBy(() -> accountService.save(account))
                .withMessage("Cannot save account with empty field(s)");

        account.setAmount(BigDecimal.valueOf(2));
        account.setCurrency(null);
        assertThatExceptionOfType(ObjectFieldsEmptyException.class)
                .isThrownBy(() -> accountService.save(account))
                .withMessage("Cannot save account with empty field(s)");
    }

    @Test
    void saveWithWrongAmount() {
        Currency currency = currencyService.getById(1L);

        Account account = new Account();
        account.setAmount(BigDecimal.valueOf(-100));
        account.setName("Test acc");
        account.setCurrency(currency);
        account.setBankName("Sber");
        account.setId(1L);

        doReturn(Optional.of(account))
                .when(accountRepo)
                .findById(1L);

        assertThatExceptionOfType(AmountSetWrongException.class)
                .isThrownBy(() -> accountService.save(account))
                .withMessage("Cannot save account with amount bigger than or equal to 0");
    }

    @Test
    void deleteById() {
        Currency currency = currencyService.getById(1L);

        Account account = new Account();
        account.setId(1L);
        account.setAmount(BigDecimal.valueOf(2));
        account.setName("Test acc");
        account.setCurrency(currency);
        account.setBankName("Sber");

        doReturn(Optional.of(account))
                .when(accountRepo)
                .findById(1L);

        accountService.deleteById(1L);

        verify(accountRepo, times(1)).deleteById(any(Long.class));
    }

    @Test
    void getByName() {
        Currency currency = currencyService.getById(1L);

        Account account = new Account();
        account.setId(1L);
        account.setAmount(BigDecimal.valueOf(2));
        account.setName("Test acc");
        account.setCurrency(currency);
        account.setBankName("Sber");

        doReturn(Optional.of(account))
                .when(accountRepo)
                .findByName("Test acc");

        Account accFromDb = accountService.getByName("Test acc");
        assertNotEquals(accFromDb, null);
        assertEquals(accFromDb.getId(), 1L);
        assertEquals(accFromDb.getBankName(), "Sber");
        assertEquals(accFromDb.getCurrency(), currency);
        assertEquals(accFromDb.getName(), "Test acc");
        assertEquals(accFromDb.getAmount(), BigDecimal.valueOf(2));

        verify(accountRepo, times(1)).findByName(any(String.class));
    }
}