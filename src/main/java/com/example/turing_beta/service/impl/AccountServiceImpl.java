package com.example.turing_beta.service.impl;

import com.example.turing_beta.entity.Account;
import com.example.turing_beta.entity.Currency;
import com.example.turing_beta.exception.exceptions.common.AmountSetWrongException;
import com.example.turing_beta.exception.exceptions.common.ObjectAlreadyExistsException;
import com.example.turing_beta.exception.exceptions.common.ObjectFieldsEmptyException;
import com.example.turing_beta.exception.exceptions.common.ObjectNotFoundException;
import com.example.turing_beta.repos.AccountRepo;
import com.example.turing_beta.service.AccountService;
import com.example.turing_beta.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepo accountRepo;
    private final CurrencyService currencyService;

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

    public void console() {
        Scanner sc = new Scanner(System.in);

        System.out.println("""
                Выберите действие:
                1. Вывести все счета
                2. Добавить новый счет
                3. Обновить существующий счет
                4. Удалить счет по id
                9. Выйти из меню Счетов""");

        int choice = sc.nextInt();

        while (choice != 9) {
            switch (choice) {
                case 1:
                    for (Account acc : getAll()) {
                        System.out.println("----------------");
                        System.out.println(acc.toString());
                    }
                    break;

                case 2:
                    sc.nextLine();
                    System.out.println("Введите название счета");
                    String name = sc.nextLine();

                    System.out.println("Введите количество денег на счету");
                    BigDecimal amount = sc.nextBigDecimal();

                    System.out.println("Введите id валюты");
                    Currency currency = currencyService.getById(sc.nextLong());

                    sc.nextLine();

                    System.out.println("Введите название банка");
                    String bankName = sc.nextLine();

                    Account accToCreate = Account.builder()
                            .name(name)
                            .amount(amount)
                            .currency(currency)
                            .bankName(bankName)
                            .build();

                    System.out.println(add(accToCreate));
                    break;

                case 3:
                    System.out.println("Введите id счета, который нужно обновить");
                    long id = sc.nextLong();

                    Account account = getById(id);

                    System.out.println("""
                            Выберите поле, которое нужно обновить
                            1. Название
                            2. Баланс
                            3. Валюта
                            4. Название банка
                            9. Выйти из меню обновления""");

                    int fieldChoice = sc.nextInt();

                    while (fieldChoice != 9) {
                        switch (fieldChoice) {
                            case 1:
                                sc.nextLine();
                                System.out.println("Введите новое название");
                                String newName = sc.nextLine();
                                account.setName(newName);
                                System.out.println("\n" + save(account) + "\n");
                                break;

                            case 2:
                                System.out.println("Введите новый баланс");
                                BigDecimal newAmount = sc.nextBigDecimal();
                                account.setAmount(newAmount);
                                System.out.println("\n" + save(account) + "\n");
                                break;

                            case 3:
                                System.out.println("Введите id валюты");
                                long newCurrencyId = sc.nextLong();
                                account.setCurrency(currencyService.getById(newCurrencyId));
                                System.out.println("\n" + save(account) + "\n");
                                break;

                            case 4:
                                sc.nextLine();
                                System.out.println("Введите новое название банка");
                                String newBankName = sc.nextLine();
                                account.setBankName(newBankName);
                                System.out.println("\n" + save(account) + "\n");
                                break;
                        }
                        System.out.println("""
                                Выберите поле, которое нужно обновить
                                1. Название
                                2. Баланс
                                3. Валюта
                                4. Название банка
                                9. Выйти из меню обновления""");

                        fieldChoice = sc.nextInt();
                    }
                    System.out.println("Вы вышли из меню обновления Счета \n");
                    break;

                case 4:
                    System.out.println("Введите id счета, который нужно удалить");
                    id = sc.nextLong();
                    deleteById(id);
                    break;
            }
            System.out.println("""
                    Выберите действие:
                    1. Вывести все счета
                    2. Добавить новый счет
                    3. Обновить существующий счет
                    4. Удалить счет по id
                    9. Выйти из меню Счетов""");

            choice = sc.nextInt();
        }

        System.out.println("Вы вышли из меню Счетов \n");
    }
}
