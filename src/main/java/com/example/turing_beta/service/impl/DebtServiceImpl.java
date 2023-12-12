package com.example.turing_beta.service.impl;

import com.example.turing_beta.entity.*;
import com.example.turing_beta.exception.exceptions.common.AmountSetWrongException;
import com.example.turing_beta.exception.exceptions.common.ObjectAlreadyExistsException;
import com.example.turing_beta.exception.exceptions.common.ObjectFieldsEmptyException;
import com.example.turing_beta.exception.exceptions.common.ObjectNotFoundException;
import com.example.turing_beta.exception.exceptions.debt.DebtWrongDueDateException;
import com.example.turing_beta.repos.DebtRepo;
import com.example.turing_beta.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class DebtServiceImpl implements DebtService {
    private final DebtRepo debtRepo;
    private final CurrencyService currencyService;
    private final ContactService contactService;
    private final DebtTypeService debtTypeService;
    @Override
    public List<Debt> getAll() {
        return debtRepo.findAll();
    }

    @Override
    public Debt add(Debt debt) {
        if (debt.getId() != null && debtRepo.existsById(debt.getId())) {
            throw new ObjectAlreadyExistsException(String.format("Debt with id = %d already exists", debt.getId()));
        }
        checkFieldsValidity(debt);

        debt = debtRepo.save(debt);
        return debt;
    }

    @Override
    public Debt getById(Long id) {
        Optional<Debt> foundDebt = debtRepo.findById(id);
        if (foundDebt.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Cannot find debt with id = %d", id));
        }
        return foundDebt.get();
    }

    @Override
    public Debt save(Debt debt) {
        Debt debtFromDb = getById(debt.getId());
        checkFieldsValidity(debt);

        debt = debtRepo.save(debt);
        return debt;
    }

    @Override
    public void deleteById(Long id) {
        Debt debt = getById(id);
        debtRepo.deleteById(id);
    }

    @Override
    public List<Debt> getAllByContactId(Long id) {
        return debtRepo.findAllByContactId(id); //todo найти все долги какого-то контакта по его id
    }

    private void checkFieldsValidity(Debt debt) throws ObjectFieldsEmptyException, AmountSetWrongException, DebtWrongDueDateException {
        if (!StringUtils.hasText(debt.getName())
                || debt.getAmount() == null
                || debt.getCurrency() == null) {
            throw new ObjectFieldsEmptyException("Cannot add debt with empty field(s)");
        }
        if (debt.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new AmountSetWrongException("Cannot add debt with amount less than or equal to 0");
        }
        if (debt.getDateDue().isBefore(debt.getDateStart())) {
            throw new DebtWrongDueDateException("Cannot add debt with due date earlier than date of start");
        }
    }

    public void console() throws ParseException {
        Scanner sc = new Scanner(System.in);

        System.out.println("""
                Выберите действие:
                1. Вывести все долги
                2. Добавить новый долг
                3. Обновить существующий долг
                4. Удалить долг по id
                5. Просмотреть все долги по id транзакции
                9. Выйти из меню Долгов""");

        int choice = sc.nextInt();

        while (choice != 9) {
            switch (choice) {
                case 1:
                    for (Debt debt : getAll()) {
                        System.out.println("----------------");
                        System.out.println(debt.toString());
                    }
                    break;

                case 2:
                    sc.nextLine();
                    System.out.println("Введите название долга");
                    String name = sc.nextLine();

                    System.out.println("Введите сумму долга");
                    BigDecimal amount = sc.nextBigDecimal();

                    System.out.println("Введите id валюты");
                    Currency currency = currencyService.getById(sc.nextLong());

                    sc.nextLine();
                    System.out.println("Введите дату начала долга в формате дд/мм/гггг");
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    LocalDateTime dateStart = format.parse(sc.nextLine()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                    sc.nextLine();
                    System.out.println("Введите дату срока долга в формате дд/мм/гггг");
                    SimpleDateFormat formatEnd = new SimpleDateFormat("dd/MM/yyyy");
                    LocalDateTime dateEnd = formatEnd.parse(sc.nextLine()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                    System.out.println("Введите id контакта, с которым связан долг");
                    Contact contact = contactService.getById(sc.nextLong());

                    System.out.println("Введите id типа долга");
                    DebtType debtType = debtTypeService.getById(sc.nextLong());

                    Debt debtToCreate = Debt.builder()
                            .name(name)
                            .amount(amount)
                            .currency(currency)
                            .dateStart(dateStart)
                            .dateDue(dateEnd)
                            .contact(contact)
                            .debtType(debtType)
                            .build();

                    System.out.println(add(debtToCreate));
                    break;

                case 3:
                    System.out.println("Введите id долга, который нужно обновить");
                    long id = sc.nextLong();

                    Debt debtToUpdate = getById(id);

                    System.out.println("""
                            Выберите поле, которое нужно обновить
                            1. Название долга
                            2. Сумма долга
                            3. Валюта
                            4. Дата начала долга
                            5. Дата окончания долга
                            6. Контакт
                            7. Тип долга
                            9. Выйти из меню обновления""");

                    int fieldChoice = sc.nextInt();

                    while (fieldChoice != 9) {
                        switch (fieldChoice) {
                            case 1:
                                sc.nextLine();
                                System.out.println("Введите новое название");
                                String newName = sc.nextLine();
                                debtToUpdate.setName(newName);
                                System.out.println("\n" + save(debtToUpdate) + "\n");
                                break;

                            case 2:
                                System.out.println("Введите новую сумму долга");
                                BigDecimal newAmount = sc.nextBigDecimal();
                                debtToUpdate.setAmount(newAmount);
                                System.out.println("\n" + save(debtToUpdate) + "\n");
                                break;

                            case 3:
                                System.out.println("Введите id валюты");
                                Currency newCurrency = currencyService.getById(sc.nextLong());
                                debtToUpdate.setCurrency(newCurrency);
                                System.out.println("\n" + save(debtToUpdate) + "\n");
                                break;

                            case 4:
                                sc.nextLine();
                                System.out.println("Введите новую дату начала долга в формате дд/мм/гггг");
                                SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy");
                                LocalDateTime newDateStart = newFormat.parse(sc.nextLine()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                                debtToUpdate.setDateStart(newDateStart);
                                System.out.println("\n" + save(debtToUpdate) + "\n");
                                break;

                            case 5:
                                sc.nextLine();
                                System.out.println("Введите новую дату окончания долга в формате дд/мм/гггг");
                                SimpleDateFormat newStartFormat = new SimpleDateFormat("dd/MM/yyyy");
                                LocalDateTime newDateEnd = newStartFormat.parse(sc.nextLine()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                                debtToUpdate.setDateStart(newDateEnd);
                                System.out.println("\n" + save(debtToUpdate) + "\n");
                                break;

                            case 6:
                                System.out.println("Введите новый id контакта");
                                long newContactId = sc.nextLong();
                                Contact newContact = contactService.getById(newContactId);
                                debtToUpdate.setContact(newContact);
                                System.out.println("\n" + save(debtToUpdate) + "\n");
                                break;

                            case 7:
                                System.out.println("Введите новый id типа долга");
                                long newDebtTypeId = sc.nextLong();
                                DebtType newDebtType = debtTypeService.getById(newDebtTypeId);
                                debtToUpdate.setDebtType(newDebtType);
                                System.out.println("\n" + save(debtToUpdate) + "\n");
                                break;
                        }
                        System.out.println("""
                            Выберите поле, которое нужно обновить
                            1. Название долга
                            2. Сумма долга
                            3. Валюта
                            4. Дата начала долга
                            5. Дата окончания долга
                            6. Контакт
                            7. Тип долга
                            9. Выйти из меню обновления""");

                        fieldChoice = sc.nextInt();
                    }
                    System.out.println("Вы вышли из меню обновления Долга \n");
                    break;

                case 4:
                    System.out.println("Введите id долга, который нужно удалить");
                    id = sc.nextLong();
                    deleteById(id);
                    break;

//                case 5:
//                    System.out.println("Введите id долга, к которому нужно привязать транзакцию");
//                    id = sc.nextLong();
//                    Debt debtToBond = getById(id);
//
//                    System.out.println("Введите id транзакции, которую нужно привязать");
//                    id = sc.nextLong();
//                    Transaction transactionToBond = transactionService.getById(id);
//
//                    debtToBond.setTransactions(debtToBond.addTransaction(transactionToBond));
//                    save(debtToBond);
//                    break;
//
//                case 6:
//                    System.out.println("Введите id долга, от которого нужно отвязать транзакцию");
//                    id = sc.nextLong();
//                    Debt debtToUnbond = getById(id);
//
//                    System.out.println("Введите id транзакции, которую нужно отвязать");
//                    id = sc.nextLong();
//                    Transaction transactionToUnbond = transactionService.getById(id);
//
//                    debtToUnbond.setTransactions(debtToUnbond.deleteTransaction(transactionToUnbond));
//                    save(debtToUnbond);
//                    break;

                case 5:
                    System.out.println("Введите id транзакции, по которой хотите получить долги");
                    id = sc.nextLong();
                    System.out.println(debtRepo.findAllByTransactionsId(id));
            }
            System.out.println("""
                Выберите действие:
                1. Вывести все долги
                2. Добавить новый долг
                3. Обновить существующий долг
                4. Удалить долг по id
                5. Просмотреть все долги по id транзакции
                9. Выйти из меню Долгов""");

            choice = sc.nextInt();
        }

        System.out.println("Вы вышли из меню Долгов \n");
    }
}
