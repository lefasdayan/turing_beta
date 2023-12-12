package com.example.turing_beta.service.impl;

import com.example.turing_beta.entity.TransactionType;
import com.example.turing_beta.exception.exceptions.common.ObjectAlreadyExistsException;
import com.example.turing_beta.exception.exceptions.common.ObjectFieldsEmptyException;
import com.example.turing_beta.exception.exceptions.common.ObjectNotFoundException;
import com.example.turing_beta.repos.TransactionTypeRepo;
import com.example.turing_beta.service.TransactionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class TransactionTypeServiceImpl implements TransactionTypeService {

    private final TransactionTypeRepo transactionTypeRepo;

    @Override
    public List<TransactionType> getAll() {
        return transactionTypeRepo.findAll();
    }

    @Override
    public TransactionType add(TransactionType transactionType) {
        if (transactionType.getId() != null && transactionTypeRepo.existsById(transactionType.getId())) {
            throw new ObjectAlreadyExistsException(String.format("Transaction type with id %d already exists", transactionType.getId()));
        }
        if (transactionType.getName() != null && transactionTypeRepo.existsByName(transactionType.getName())) {
            throw new ObjectAlreadyExistsException(String.format("Transaction type with name %s already exists", transactionType.getName()));
        }

        transactionType = transactionTypeRepo.save(transactionType);
        return transactionType;
    }

    @Override
    public TransactionType getById(Long id) {
        Optional<TransactionType> foundTransactionType = transactionTypeRepo.findById(id);
        if (foundTransactionType.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Could not find a transaction type with id %d", id));
        }
        return foundTransactionType.get();
    }

    @Override
    public TransactionType save(TransactionType transactionType) {
        TransactionType transactionTypeFromDb = getById(transactionType.getId());

        if (!StringUtils.hasText(transactionType.getName())) {
            throw new ObjectFieldsEmptyException("Transaction type name field is empty.");
        }
        transactionType = transactionTypeRepo.save(transactionType);
        return transactionType;
    }

    @Override
    public TransactionType getByName(String name) {
        Optional<TransactionType> foundTransactionType = transactionTypeRepo.findByName(name);
        if (foundTransactionType.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Could not find a transaction type with name %s", name));
        }
        return foundTransactionType.get();
    }

    @Override
    public void deleteById(Long id) {
        TransactionType transactionType = getById(id);
        transactionTypeRepo.deleteById(id);
    }

    public void console() {
        Scanner sc = new Scanner(System.in);

        System.out.println("""
                Выберите действие:
                1. Вывести все типы транзакций
                2. Добавить новый тип транзакции
                3. Обновить существующий тип транзакции
                4. Удалить тип транзакции по id
                9. Выйти из меню Типов транзакций""");

        int choice = sc.nextInt();

        while (choice != 9) {
            switch (choice) {
                case 1:
                    for (TransactionType type : getAll()) {
                        System.out.println("----------------");
                        System.out.println(type.toString());
                    }
                    break;

                case 2:
                    sc.nextLine();
                    System.out.println("Введите название типа транзакции");
                    String name = sc.nextLine();

                    sc.nextLine();
                    System.out.println("Введите заметку к типу транзакции");
                    String note = sc.nextLine();

                    TransactionType transactionTypeToCreate = TransactionType.builder()
                            .name(name)
                            .note(note)
                            .build();

                    System.out.println(add(transactionTypeToCreate));
                    break;

                case 3:
                    System.out.println("Введите id типа, который нужно обновить");
                    long id = sc.nextLong();

                    TransactionType transactionTypeToUpdate = getById(id);

                    System.out.println("""
                            Выберите поле, которое нужно обновить
                            1. Название
                            2. Заметка
                            9. Выйти из меню обновления""");

                    int fieldChoice = sc.nextInt();

                    while (fieldChoice != 9) {
                        switch (fieldChoice) {
                            case 1:
                                sc.nextLine();
                                System.out.println("Введите новое название");
                                String newName = sc.nextLine();
                                transactionTypeToUpdate.setName(newName);
                                System.out.println("\n" + save(transactionTypeToUpdate) + "\n");
                                break;

                            case 2:
                                sc.nextLine();
                                System.out.println("Введите новую заметку");
                                String newNote = sc.nextLine();
                                transactionTypeToUpdate.setNote(newNote);
                                System.out.println("\n" + save(transactionTypeToUpdate) + "\n");
                                break;

                        }
                        System.out.println("""
                            Выберите поле, которое нужно обновить
                            1. Название
                            2. Заметка
                            9. Выйти из меню обновления""");

                        fieldChoice = sc.nextInt();
                    }
                    System.out.println("Вы вышли из меню обновления Типа транзакции \n");
                    break;

                case 4:
                    System.out.println("Введите id типа, который нужно удалить");
                    id = sc.nextLong();
                    deleteById(id);
                    break;
            }
            System.out.println("""
                Выберите действие:
                1. Вывести все типы транзакций
                2. Добавить новый тип транзакции
                3. Обновить существующий тип транзакции
                4. Удалить тип транзакции по id
                9. Выйти из меню Типов транзакций""");

            choice = sc.nextInt();
        }

        System.out.println("Вы вышли из меню Типов транзакций \n");
    }
}
