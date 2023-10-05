package com.example.turing_beta.service.impl;

import com.example.turing_beta.entity.Account;
import com.example.turing_beta.entity.Currency;
import com.example.turing_beta.exception.exceptions.account.AccountCurrencyChangingException;
import com.example.turing_beta.exception.exceptions.common.AmountSetWrongException;
import com.example.turing_beta.exception.exceptions.common.ObjectAlreadyExistsException;
import com.example.turing_beta.exception.exceptions.common.ObjectFieldsEmptyException;
import com.example.turing_beta.exception.exceptions.common.ObjectNotFoundException;
import com.example.turing_beta.repos.AccountRepo;
import com.example.turing_beta.repos.CurrencyRepo;
import com.example.turing_beta.rest.dto.account.AccountAllFields;
import com.example.turing_beta.rest.dto.account.NewAccountRequest;
import com.example.turing_beta.rest.dto.account.UpdateAccountRequest;
import com.example.turing_beta.service.AccountService;
import com.example.turing_beta.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AccountServiceImplTest {
    private AccountService accountService;

    private CurrencyService currencyService;

    @Captor
    private ArgumentCaptor<Account> accountArgumentCaptor;

    @Mock
    private AccountRepo accountRepo;

    @Mock
    private CurrencyRepo currencyRepo;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        this.currencyService = new CurrencyServiceImpl(currencyRepo);
        this.accountService = new AccountServiceImpl(accountRepo, currencyService);
    }

    @Test
    void itShouldSuccessfullyGetAllAccounts() {
        //...return a currency when query is sent
        Currency currency = new Currency();
        currency.setId(1L);
        currency.setName("Rubble");
        currency.setCourseToRubble(BigDecimal.valueOf(1));

        given(currencyRepo.findById(1L))
                .willReturn(Optional.of(currency));

        //Given two valid accounts
        Account acc1 = Account.builder()
                .id(1L)
                .name("Test 1")
                .amount(BigDecimal.valueOf(2))
                .currency(currencyService.getById(1L))
                .bankName("Tinkoff")
                .build();

        Account acc2 = Account.builder()
                .id(2L)
                .name("Test 2")
                .amount(BigDecimal.valueOf(220))
                .currency(currencyService.getById(1L))
                .bankName("Sber")
                .build();

        //...expected list of accounts
        List<Account> accounts = new ArrayList<>(Arrays.asList(acc1, acc2));
        given(accountRepo.findAll())
                .willReturn(accounts);

        //When
        List<AccountAllFields> accountsFromDb = accountService.getAll();

        assertNotEquals(accountsFromDb.size(), 0);

        for (AccountAllFields account : accountsFromDb) {
            assertNotEquals(account, null);
        }
        verify(currencyRepo, times(2)).findById(1L);
        verify(accountRepo, times(1)).findAll();
    }

    @Test
    void itShouldSuccessfullyAddNewAccount() {
        //Given a valid account
        NewAccountRequest request = NewAccountRequest.builder()
                .name("Test account")
                .amount(BigDecimal.valueOf(1240.66))
                .currencyId(1L)
                .build();

        //...return a currency when query is sent
        Currency currency = new Currency();
        currency.setId(1L);
        currency.setName("Rubble");
        currency.setCourseToRubble(BigDecimal.valueOf(1));

        given(currencyRepo.findById(request.getCurrencyId()))
                .willReturn(Optional.of(currency));

        //...an expected account
        Account account = Account.builder()
                .name("Test account")
                .amount(BigDecimal.valueOf(1240.66))
                .currency(currencyService.getById(1L))
                .build();

        //...return updated account when querying db
        Account accountFromDb = Account.builder()
                .id(1L)
                .name("Test account")
                .amount(BigDecimal.valueOf(1240.66))
                .currency(currencyService.getById(1L))
                .build();
        given(accountRepo.save(any(Account.class)))
                .willReturn(accountFromDb);

        //When
        accountService.add(request);

        //Then
        then(accountRepo).should().save(accountArgumentCaptor.capture()); //todo ???
        Account captoredAcc = accountArgumentCaptor.getValue();
        assertThat(account).isEqualTo(captoredAcc);
    }

    @Test
    void itShouldNeverSaveAccountWithAlreadyTakenName() {
        // Given a new Account to add with a name already taken
        NewAccountRequest request = NewAccountRequest.builder()
                .name("Test take")
                .amount(BigDecimal.valueOf(1240.66))
                .currencyId(1L)
                .build();

        Currency currency = new Currency();
        currency.setId(1L);
        currency.setName("Rubble");
        currency.setCourseToRubble(BigDecimal.valueOf(1));

        //... return a currency when query is sent
        given(currencyRepo.findById(request.getCurrencyId()))
                .willReturn(Optional.of(currency));

        //...return existing account with same name
        Account accountFromDb = Account.builder()
                .id(1L)
                .name("Test take")
                .amount(BigDecimal.valueOf(1240.66))
                .currency(currencyService.getById(1L))
                .build();

        given(accountRepo.findByName(request.getName()))
                .willReturn(Optional.of(accountFromDb));

        //Then
        assertThatThrownBy(() -> accountService.add(request))
                .isInstanceOf(ObjectAlreadyExistsException.class)
                .hasMessageContaining(String.format("Cannot save. " +
                        "Account with name \"%s\" already exists", request.getName()));

        then(accountRepo).should(never()).save(any(Account.class));
    }

    @Test
    void itShouldNeverAddAccountWhenFieldIsEmpty() {
        // Given a new Account to add without a name
        NewAccountRequest request = NewAccountRequest.builder()
                .amount(BigDecimal.valueOf(1240.66))
                .currencyId(1L)
                .build();

        Currency currency = new Currency();
        currency.setId(1L);
        currency.setName("Rubble");
        currency.setCourseToRubble(BigDecimal.valueOf(1));

        //... return a currency when query is sent
        given(currencyRepo.findById(request.getCurrencyId()))
                .willReturn(Optional.of(currency));
        // Then
        assertThatThrownBy(() -> accountService.add(request))
                .isInstanceOf(ObjectFieldsEmptyException.class)
                .hasMessageContaining("Cannot save account with empty field(s)");

        then(accountRepo).should(never()).save(any(Account.class));

        //Given a new Account to add w/o an amount
        request.setName("Test acc");
        request.setAmount(null);

        //When
        //Then
        assertThatThrownBy(() -> accountService.add(request))
                .isInstanceOf(ObjectFieldsEmptyException.class)
                .hasMessageContaining("Cannot save account with empty field(s)");

        then(accountRepo).should(never()).save(any(Account.class));

        //Given a new Account to add w/o a currency
        request.setAmount(BigDecimal.valueOf(1));
        request.setCurrencyId(null);

        //When
        //Then
        assertThatThrownBy(() -> accountService.add(request))
                .isInstanceOf(ObjectFieldsEmptyException.class)
                .hasMessageContaining("Cannot save account with empty field(s)");

        then(accountRepo).should(never()).save(any(Account.class));
    }

    @Test
    void itShouldNeverAddAccountWhenAmountIsWrong() {
        // Given a new Account to add with wrong amount of money
        NewAccountRequest request = NewAccountRequest.builder()
                .name("test account")
                .amount(BigDecimal.valueOf(-1250))
                .currencyId(1L)
                .build();

        Currency currency = new Currency();
        currency.setId(1L);
        currency.setName("Rubble");
        currency.setCourseToRubble(BigDecimal.valueOf(1));

        // When
        given(currencyRepo.findById(request.getCurrencyId()))
                .willReturn(Optional.of(currency));
        // Then
        assertThatThrownBy(() -> accountService.add(request))
                .isInstanceOf(AmountSetWrongException.class)
                .hasMessageContaining("Cannot save account with amount less than or equal to 0");

        then(accountRepo).should(never()).save(any(Account.class));
    }

    @Test
    void itShouldSuccessfullyReturnAccountById() {
        //...return a currency when query is sent
        Currency currency = new Currency();
        currency.setId(1L);
        currency.setName("Rubble");
        currency.setCourseToRubble(BigDecimal.valueOf(1));

        given(currencyRepo.findById(1L))
                .willReturn(Optional.of(currency));

        //...return an account with id=1 from db
        Account account = Account.builder()
                .id(1L)
                .name("test acc")
                .amount(BigDecimal.valueOf(1250))
                .currency(currencyService.getById(1L))
                .bankName("Sber")
                .build();

        given(accountRepo.findById(any(Long.class)))
                .willReturn(Optional.of(account));

        //...expected account
        AccountAllFields expectedAcc = AccountAllFields.builder()
                .id(1L)
                .name("test acc")
                .amount(BigDecimal.valueOf(1250))
                .currencyName(currencyService.getById(1L).getName())
                .bankName("Sber")
                .build();

        //When
        AccountAllFields accFromDb = accountService.getById(1L);

        //Then
        assertNotEquals(accFromDb, null);
        assertEquals(accFromDb.getId(), 1L);
        assertEquals(accFromDb.getName(), "test acc");
        assertEquals(accFromDb.getAmount(), BigDecimal.valueOf(1250));
        assertEquals(accFromDb.getCurrencyName(), currencyService.getById(1L).getName());
        assertEquals(accFromDb.getBankName(), "Sber");

        verify(accountRepo, times(1)).findById(any(Long.class));
    }

    @Test
    void itShouldNeverReturnAnAccountWhenThereIsNoAccountWithSuchId() {
        //Given
        given(accountRepo.findById(100L))
                .willReturn(Optional.empty());

        //Then
        assertThatThrownBy(() -> accountService.getById(100L))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining(String.format("Cannot find account with id = %d", 100L));
    }

    @Test
    void itShouldSuccessfullySaveAccount() {
        //...return a currency when query is sent
        Currency currency = new Currency();
        currency.setId(1L);
        currency.setName("Rubble");
        currency.setCourseToRubble(BigDecimal.valueOf(1));

        given(currencyRepo.findById(1L))
                .willReturn(Optional.of(currency));
        given(currencyRepo.findByName("Rubble"))
                .willReturn(Optional.of(currency));

        //Given a valid account update
        UpdateAccountRequest request = UpdateAccountRequest.builder()
                .id(1L)
                .name("Test account after")
                .amount(BigDecimal.valueOf(1240.66))
                .currencyId(1L)
                .bankName("Sber")
                .build();

        //... and account with ID=1 from db
        Account accFromDb = Account.builder()
                .id(1L)
                .name("Test acc before")
                .amount(BigDecimal.valueOf(1240.66))
                .currency(currencyService.getById(1L))
                .bankName("Sber")
                .build();

        //... expected updated account
        Account updatedAcc = Account.builder()
                .id(1L)
                .name("Test account after")
                .amount(BigDecimal.valueOf(1240.66))
                .currency(currencyService.getById(1L))
                .bankName("Sber")
                .build();

        //... return accFromDb for querying repo
        given(accountRepo.findById(request.getId()))
                .willReturn(Optional.of(accFromDb));

        //... return updated account when save() is triggered
        given(accountRepo.save(any(Account.class)))
                .willReturn(updatedAcc);

        //When
        accountService.save(request);

        //Then
        then(accountRepo).should().save(accountArgumentCaptor.capture()); //todo ???
        Account captoredAcc = accountArgumentCaptor.getValue();
        assertThat(updatedAcc).isEqualTo(captoredAcc);
    }

    @Test
    void itShouldNeverSaveAnAccountWithEmptyFields() {
        //Given
        //...return a currency when query is sent
        Currency currency = new Currency();
        currency.setId(1L);
        currency.setName("Rubble");
        currency.setCourseToRubble(BigDecimal.valueOf(1));

        given(currencyRepo.findById(1L))
                .willReturn(Optional.of(currency));
        given(currencyRepo.findByName("Rubble"))
                .willReturn(Optional.of(currency));
        //...existing account in DB
        Account accFromDb = Account.builder()
                .id(1L)
                .name("Test before")
                .amount(BigDecimal.valueOf(1250))
                .currency(currency)
                .bankName("Sber")
                .build();

        given(accountRepo.findById(1L))
                .willReturn(Optional.of(accFromDb));

        //...updated account without id
        UpdateAccountRequest request = UpdateAccountRequest.builder()
                .id(null)
                .name("Test")
                .amount(BigDecimal.valueOf(1250))
                .currencyId(1L)
                .bankName("Sber")
                .build();

        //Then
        assertThatThrownBy(() -> accountService.save(request))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining(String.format("Cannot find account with id = %d", request.getId()));
        then(accountRepo).should(never()).save(any(Account.class));

        //...given a request w/o name
        request.setId(1L);
        request.setName(null);

        //Then
        assertThatThrownBy(() -> accountService.save(request))
                .isInstanceOf(ObjectFieldsEmptyException.class)
                .hasMessageContaining("Cannot save account with empty field(s)");
        then(accountRepo).should(never()).save(any(Account.class));

        //..given a request w/o amount
        request.setName("test");
        request.setAmount(null);

        //Then
        assertThatThrownBy(() -> accountService.save(request))
                .isInstanceOf(ObjectFieldsEmptyException.class)
                .hasMessageContaining("Cannot save account with empty field(s)");
        then(accountRepo).should(never()).save(any(Account.class));

        //given a request w/o a currency
        request.setAmount(BigDecimal.valueOf(1250));
        request.setCurrencyId(null);

        //Then
        assertThatThrownBy(() -> accountService.save(request))
                .isInstanceOf(ObjectFieldsEmptyException.class)
                .hasMessageContaining("Cannot save account with empty field(s)");
        then(accountRepo).should(never()).save(any(Account.class));
    }

    @Test
    void itShouldNotSaveAccountWithWrongAmount() {
        //Given
        //...return a currency when query is sent
        Currency currency = new Currency();
        currency.setId(1L);
        currency.setName("Rubble");
        currency.setCourseToRubble(BigDecimal.valueOf(1));

        given(currencyRepo.findById(1L))
                .willReturn(Optional.of(currency));
        given(currencyRepo.findByName("Rubble"))
                .willReturn(Optional.of(currency));
        //...existing account in DB
        Account accFromDb = Account.builder()
                .id(1L)
                .name("Test before")
                .amount(BigDecimal.valueOf(1248))
                .currency(currency)
                .bankName("Sber")
                .build();

        given(accountRepo.findById(1L))
                .willReturn(Optional.of(accFromDb));

        //...updated account with wrong amount of money
        UpdateAccountRequest request = UpdateAccountRequest.builder()
                .id(1L)
                .name("Test")
                .amount(BigDecimal.valueOf(-23000))
                .currencyId(1L)
                .bankName("Sber")
                .build();

        //Then
        assertThatThrownBy(() -> accountService.save(request))
                .isInstanceOf(AmountSetWrongException.class)
                .hasMessageContaining("Cannot save account with amount less than or equal to 0");

        then(accountRepo).should(never()).save(any(Account.class));
    }

    @Test
    void itShouldNeverSaveAccountWhenCurrencyWasChanged() {
        //Given
        //...return a currencies when query is sent
        Currency rubble = new Currency();
        rubble.setId(1L);
        rubble.setName("Rubble");
        rubble.setCourseToRubble(BigDecimal.valueOf(1));

        given(currencyRepo.findById(1L))
                .willReturn(Optional.of(rubble));
        given(currencyRepo.findByName("Rubble"))
                .willReturn(Optional.of(rubble));

        Currency dollar = new Currency();
        dollar.setId(2L);
        dollar.setName("Dollar");
        dollar.setCourseToRubble(BigDecimal.valueOf(99.82));

        given(currencyRepo.findById(2L))
                .willReturn(Optional.of(dollar));
        given(currencyRepo.findByName("Dollar"))
                .willReturn(Optional.of(dollar));

        //...existing account in DB
        Account accFromDb = Account.builder()
                .id(1L)
                .name("Test before")
                .amount(BigDecimal.valueOf(1250))
                .currency(rubble)
                .bankName("Sber")
                .build();

        given(accountRepo.findById(1L))
                .willReturn(Optional.of(accFromDb));

        //...updated account with changed currency
        UpdateAccountRequest request = UpdateAccountRequest.builder()
                .id(1L)
                .name("Test")
                .amount(BigDecimal.valueOf(1260))
                .currencyId(2L)
                .bankName("Sber")
                .build();

        //Then
        assertThatThrownBy(() -> accountService.save(request))
                .isInstanceOf(AccountCurrencyChangingException.class)
                .hasMessageContaining("Cannot change currency of an account after it's creation");

        then(accountRepo).should(never()).save(any(Account.class));
    }

    @Test
    void itShouldNeverSaveAccountWithNameAlreadyTaken() {
        //Given
        //...return a currencies when query is sent
        Currency rubble = new Currency();
        rubble.setId(1L);
        rubble.setName("Rubble");
        rubble.setCourseToRubble(BigDecimal.valueOf(1));

        given(currencyRepo.findById(1L))
                .willReturn(Optional.of(rubble));
        given(currencyRepo.findByName("Rubble"))
                .willReturn(Optional.of(rubble));

        //...existing account in DB with name already taken
        Account accFromDbWithOriginalName = Account.builder()
                .id(1L)
                .name("Taken name")
                .amount(BigDecimal.valueOf(1250))
                .currency(rubble)
                .bankName("Sber")
                .build();

        given(accountRepo.findById(1L))
                .willReturn(Optional.of(accFromDbWithOriginalName));

        //...existing account in DB to update
        Account accFromDb = Account.builder()
                .id(2L)
                .name("First name")
                .amount(BigDecimal.valueOf(1250))
                .currency(rubble)
                .bankName("Sber")
                .build();

        given(accountRepo.findById(2L))
                .willReturn(Optional.of(accFromDb));

        //...updated account with changed currency
        UpdateAccountRequest request = UpdateAccountRequest.builder()
                .id(2L)
                .name("Taken name")
                .amount(BigDecimal.valueOf(1260))
                .currencyId(2L)
                .bankName("Sber")
                .build();

        //Then
        assertThatThrownBy(() -> accountService.save(request))
                .isInstanceOf(AccountCurrencyChangingException.class)
                .hasMessageContaining("Cannot change currency of an account after it's creation");

        then(accountRepo).should(never()).save(any(Account.class));
    }

    @Test
    void itShouldSuccessfullyDeleteAccountById() {
        //Given
        //...return a currencies when query is sent
        Currency rubble = new Currency();
        rubble.setId(1L);
        rubble.setName("Rubble");
        rubble.setCourseToRubble(BigDecimal.valueOf(1));

        given(currencyRepo.findById(1L))
                .willReturn(Optional.of(rubble));

        //...existing account in DB
        Account accFromDb = Account.builder()
                .id(1L)
                .name("Taken name")
                .amount(BigDecimal.valueOf(1250))
                .currency(rubble)
                .bankName("Sber")
                .build();

        given(accountRepo.findById(1L))
                .willReturn(Optional.of(accFromDb));

        accountService.deleteById(1L);

        verify(accountRepo, times(1)).deleteById(any(Long.class));
    }

    @Test
    void itShouldSuccessfullyReturnAccountByName() {
        //Given
        //...return a currencies when query is sent
        Currency rubble = new Currency();
        rubble.setId(1L);
        rubble.setName("Rubble");
        rubble.setCourseToRubble(BigDecimal.valueOf(1));

        given(currencyRepo.findById(1L))
                .willReturn(Optional.of(rubble));

        //...existing account in DB with matching name
        Account accFromDb = Account.builder()
                .id(1L)
                .name("Taken name")
                .amount(BigDecimal.valueOf(1250))
                .currency(rubble)
                .bankName("Sber")
                .build();

        given(accountRepo.findByName("Taken name"))
                .willReturn(Optional.of(accFromDb));

        //...expected account
        AccountAllFields expectedAcc = AccountAllFields.builder()
                .id(1L)
                .name("Taken name")
                .amount(BigDecimal.valueOf(1250))
                .currencyName(currencyService.getById(1L).getName())
                .bankName("Sber")
                .build();

        //When
        AccountAllFields accGotByMethod = accountService.getByName("Taken name");

        //Then
        assertNotEquals(accGotByMethod, null);
        assertEquals(accGotByMethod.getId(), 1L);
        assertEquals(accGotByMethod.getName(), "Taken name");
        assertEquals(accGotByMethod.getAmount(), BigDecimal.valueOf(1250));
        assertEquals(accGotByMethod.getCurrencyName(), currencyService.getById(1L).getName());
        assertEquals(accGotByMethod.getBankName(), "Sber");

        verify(accountRepo, times(1)).findByName(any(String.class));
    }

    @Test
    void itShouldNotReturnAccountWithNameNotTaken() {
        //Given
        given(accountRepo.findByName("Test acc"))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getByName("Test acc"))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining(String.format("Cannot find account with name %s", "Test acc"));

        verify(accountRepo, times(1)).findByName(any(String.class));
    }
}