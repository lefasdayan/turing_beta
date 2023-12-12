package com.example.turing_beta.service.impl;

import com.example.turing_beta.entity.*;
import com.example.turing_beta.exception.exceptions.common.AmountSetWrongException;
import com.example.turing_beta.exception.exceptions.common.ObjectAlreadyExistsException;
import com.example.turing_beta.exception.exceptions.common.ObjectFieldsEmptyException;
import com.example.turing_beta.exception.exceptions.common.ObjectNotFoundException;
import com.example.turing_beta.exception.exceptions.transaction.TransactionCredentialsSetWrongException;
import com.example.turing_beta.repos.TransactionRepo;
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
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepo transactionRepo;
    private final CurrencyService currencyService;
    private final TransactionTypeService transactionTypeService;
    private final AccountService accountService;
    private final DebtService debtService;

    @Override
    public List<Transaction> getAll() {
        return transactionRepo.findAll();
    }

    @Override
    public Transaction add(Transaction transaction) {
        checkFieldsValidity(transaction);

        if (transaction.getId() != null && transactionRepo.existsById(transaction.getId())) {
            throw new ObjectAlreadyExistsException(String.format("Transaction with id = %d already exists", transaction.getId()));
        }

        if (transaction.getFromAcc() != null) {
            transaction.getFromAcc().setAmount(transaction.getFromAcc().getAmount().subtract(transaction.getAmount()));
            accountService.save(transaction.getFromAcc());
        }

        if (transaction.getToAcc() != null) {
            transaction.getToAcc().setAmount(transaction.getToAcc().getAmount().add(transaction.getAmount()));
            accountService.save(transaction.getToAcc());
        }

        transaction = transactionRepo.save(transaction);
        return transaction;
    }

    @Override
    public Transaction getById(Long id) {
        Optional<Transaction> foundTransaction = transactionRepo.findById(id);
        if (foundTransaction.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Could not find a transaction with name %d", id));
        }
        return foundTransaction.get();
    }

    @Override
    public Transaction save(Transaction transaction) {
        Transaction transactionFromDb = getById(transaction.getId());

        checkFieldsValidity(transaction);

        if (transaction.getFromAcc() != null) {
            transaction.getFromAcc().setAmount(transaction.getFromAcc().getAmount().add(transactionFromDb.getAmount()).subtract(transaction.getAmount()));
            accountService.save(transaction.getFromAcc());
        }

        if (transaction.getToAcc() != null) {
            transaction.getToAcc().setAmount(transaction.getToAcc().getAmount().subtract(transactionFromDb.getAmount()).add(transaction.getAmount()));
            accountService.save(transaction.getToAcc());
        }

        transaction = transactionRepo.save(transaction);
        return transaction;
    }

    @Override
    public void deleteById(Long id) {
        Transaction transactionFromDb = getById(id);

        if (transactionFromDb.getFromAcc() != null) {
            transactionFromDb.getFromAcc().setAmount(transactionFromDb.getFromAcc().getAmount().add(transactionFromDb.getAmount()));
        }

        if (transactionFromDb.getToAcc() != null) {
            transactionFromDb.getToAcc().setAmount(transactionFromDb.getToAcc().getAmount().subtract(transactionFromDb.getAmount()));
        }

        transactionRepo.deleteById(id);
    }

    private void checkFieldsValidity(Transaction transaction)
            throws ObjectAlreadyExistsException, ObjectFieldsEmptyException,
            TransactionCredentialsSetWrongException, AmountSetWrongException {
        if (!StringUtils.hasText(transaction.getName())
                || transaction.getAmount() == null
                || transaction.getCurrency() == null) {
            throw new ObjectFieldsEmptyException("Cannot add transaction with empty field(s)");
        }
        if (transaction.getFromAcc() == null && transaction.getToAcc() == null) {
            throw new TransactionCredentialsSetWrongException("Cannot add transaction without any account assigned");
        }
        if (transaction.getFromAcc().equals(transaction.getToAcc())) {
            throw new TransactionCredentialsSetWrongException("Cannot transfer money from and to the same account");
        }
        if (transaction.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new AmountSetWrongException("Cannot add transaction with amount less than or equal to 0");
        }
    }

    public void console() throws ParseException {
        Scanner sc = new Scanner(System.in);

        System.out.println("""
                Выберите действие:
                1. Вывести все транзакции
                2. Добавить новую транзакцию
                3. Обновить существующую транзакцию
                4. Удалить транзакцию по id
                5. Привязать долг к транзакции
                6. Удалить долг, привязанный к транзакции
                7. Просмотреть все транзакции по id долга
                9. Выйти из меню Транзакций""");

        int choice = sc.nextInt();

        while (choice != 9) {
            switch (choice) {
                case 1:
                    for (Transaction transaction : getAll()) {
                        System.out.println("----------------");
                        System.out.println(transaction.toString());
                    }
                    break;

                case 2:
                    sc.nextLine();
                    System.out.println("Введите название транзакции");
                    String name = sc.nextLine();

                    System.out.println("Введите сумму транзакции");
                    BigDecimal amount = sc.nextBigDecimal();

                    System.out.println("Введите id валюты");
                    Currency currency = currencyService.getById(sc.nextLong());

                    sc.nextLine();
                    System.out.println("Введите дату и время транзакции в формате дд/мм/гггг чч:мм");
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                    LocalDateTime dateTime = format.parse(sc.nextLine()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                    System.out.println("Введите id типа транзакции");
                    TransactionType transactionType = transactionTypeService.getById(sc.nextLong());

                    System.out.println("Введите id счета, с которого переводились деньги (0, если было зачисление)");
                    long fromAccId = sc.nextLong();
                    Account fromAccount = null;
                    if (fromAccId != 0L){
                       fromAccount = accountService.getById(fromAccId);
                    }

                    System.out.println("Введите id счета, на который переводились деньги (0, если было списание)");
                    long toAccId = sc.nextLong();
                    Account toAccount = null;
                    if (toAccId != 0L){
                        toAccount = accountService.getById(toAccId);
                    }

                    Transaction transactionToCreate = Transaction.builder()
                            .name(name)
                            .amount(amount)
                            .currency(currency)
                            .dateTime(dateTime)
                            .type(transactionType)
                            .fromAcc(fromAccount)
                            .toAcc(toAccount)
                            .build();

                    System.out.println(add(transactionToCreate));
                    break;

                case 3:
                    System.out.println("Введите id транзакции, которую нужно обновить");
                    long id = sc.nextLong();

                    Transaction transaction = getById(id);

                    System.out.println("""
                            Выберите поле, которое нужно обновить
                            1. Название транзакции
                            2. Сумма транзакции
                            3. Валюта
                            4. Дата и время
                            5. Счет списания
                            6. Счет зачисления
                            9. Выйти из меню обновления""");

                    int fieldChoice = sc.nextInt();

                    while (fieldChoice != 9) {
                        switch (fieldChoice) {
                            case 1:
                                sc.nextLine();
                                System.out.println("Введите новое имя");
                                String newName = sc.nextLine();
                                transaction.setName(newName);
                                System.out.println("\n" + save(transaction) + "\n");
                                break;

                            case 2:
                                System.out.println("Введите новую сумму транзакции");
                                BigDecimal newAmount = sc.nextBigDecimal();
                                transaction.setAmount(newAmount);
                                System.out.println("\n" + save(transaction) + "\n");
                                break;

                            case 3:
                                System.out.println("Введите id валюты");
                                Currency newCurrency = currencyService.getById(sc.nextLong());
                                transaction.setCurrency(newCurrency);
                                System.out.println("\n" + save(transaction) + "\n");
                                break;

                            case 4:
                                sc.nextLine();
                                System.out.println("Введите новую дату и время транзакции в формате дд/мм/гггг чч:мм");
                                SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                                LocalDateTime newDateTime = newFormat.parse(sc.nextLine()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                                transaction.setDateTime(newDateTime);
                                System.out.println("\n" + save(transaction) + "\n");
                                break;

                            case 5:
                                System.out.println("Введите новый id счета, с которого переводились деньги (0, если было зачисление)");
                                long newFromAccId = sc.nextLong();
                                Account newFromAccount = null;
                                if (newFromAccId != 0L){
                                    newFromAccount = accountService.getById(newFromAccId);
                                }
                                transaction.setFromAcc(newFromAccount);
                                System.out.println("\n" + save(transaction) + "\n");
                                break;

                            case 6:
                                System.out.println("Введите новый id счета, на который переводились деньги (0, если было списание)");
                                long newToAccId = sc.nextLong();
                                Account newToAccount = null;
                                if (newToAccId != 0L){
                                    newToAccount = accountService.getById(newToAccId);
                                }
                                transaction.setToAcc(newToAccount);
                                System.out.println("\n" + save(transaction) + "\n");
                                break;
                        }
                        System.out.println("""
                                Выберите поле, которое нужно обновить
                                1. Название транзакции
                                2. Сумма транзакции
                                3. Валюта
                                4. Дата и время
                                5. Счет списания
                                6. Счет зачисления
                                9. Выйти из меню обновления""");

                        fieldChoice = sc.nextInt();
                    }
                    System.out.println("Вы вышли из меню обновления Транзакции \n");
                    break;

                case 4:
                    System.out.println("Введите id транзакции, которую нужно удалить");
                    id = sc.nextLong();
                    deleteById(id);
                    break;

                case 5:
                    System.out.println("Введите id транзакции, к которой нужно привязать долг");
                    id = sc.nextLong();
                    Transaction transactionToBond = getById(id);

                    System.out.println("Введите id долга, который нужно привязать");
                    id = sc.nextLong();
                    Debt debtToBond = debtService.getById(id);

                    transactionToBond.setDebts(transactionToBond.addDebt(debtToBond));
                    save(transactionToBond);
                    break;

                case 6:
                    System.out.println("Введите id транзакции, от которой нужно отвязать долг");
                    id = sc.nextLong();
                    Transaction transactionToUnbond = getById(id);

                    System.out.println("Введите id долга, который нужно отвязать");
                    id = sc.nextLong();
                    Debt debtToUnbond = debtService.getById(id);

                    transactionToUnbond.setDebts(transactionToUnbond.deleteDebt(debtToUnbond));
                    save(transactionToUnbond);
                    break;

                case 7:
                    System.out.println("Введите id долга, по которому хотите получить транзакции");
                    id = sc.nextLong();
                    System.out.println(transactionRepo.findAllByDebtsId(id));
            }
            System.out.println("""
                    Выберите действие:
                    1. Вывести все транзакции
                    2. Добавить новую транзакцию
                    3. Обновить существующую транзакцию
                    4. Удалить транзакцию по id
                    5. Привязать долг к транзакции
                    6. Удалить долг, привязанный к транзакции
                    7. Просмотреть все транзакции по id долга
                    9. Выйти из меню Транзакций""");

            choice = sc.nextInt();
        }

        System.out.println("Вы вышли из меню Транзакций \n");
    }
}
