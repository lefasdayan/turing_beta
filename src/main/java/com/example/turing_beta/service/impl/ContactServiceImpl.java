package com.example.turing_beta.service.impl;

import com.example.turing_beta.entity.Account;
import com.example.turing_beta.entity.Contact;
import com.example.turing_beta.entity.Currency;
import com.example.turing_beta.exception.exceptions.common.ObjectAlreadyExistsException;
import com.example.turing_beta.exception.exceptions.common.ObjectFieldsEmptyException;
import com.example.turing_beta.exception.exceptions.common.ObjectNotFoundException;
import com.example.turing_beta.repos.ContactRepo;
import com.example.turing_beta.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactRepo contactRepo;

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contact add(Contact contact) {
        if (contact.getId() != null && contactRepo.existsById(contact.getId())) {
            throw new ObjectAlreadyExistsException(String.format("Contact with id %d already exists", contact.getId()));
        }
        if (!StringUtils.hasText(contact.getName())) {
            throw new ObjectFieldsEmptyException("Cannot add contact with empty field(s)");
        }
        contact = contactRepo.save(contact);
        return contact;
    }

    @Override
    public Contact save(Contact contact) {
        Contact contactFromDb = getById(contact.getId());
        if (!StringUtils.hasText(contact.getName())) {
            throw new ObjectFieldsEmptyException("Cannot add contact with empty field(s)");
        }
        contact = contactRepo.save(contact);
        return contact;
    }

    @Override
    public void deleteById(Long id) {
        Contact contact = getById(id);
        contactRepo.deleteById(id);
    }

    @Override
    public Contact getById(Long id) {
        Optional<Contact> foundContact = contactRepo.findById(id);
        if (foundContact.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Cannot find contact with id = %d", id));
        }
        return foundContact.get();
    }

    @Override
    public Contact getByName(String name) {
        Optional<Contact> foundContact = contactRepo.findByName(name);
        if (foundContact.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Cannot find contact with name %s", name));
        }
        return foundContact.get();
    }

    public void console() {
        Scanner sc = new Scanner(System.in);

        System.out.println("""
                Выберите действие:
                1. Вывести все контакты
                2. Добавить новый контакт
                3. Обновить существующий контакт
                4. Удалить контакт по id
                9. Выйти из меню Контактов""");

        int choice = sc.nextInt();

        while (choice != 9) {
            switch (choice) {
                case 1:
                    for (Contact contact : getAll()) {
                        System.out.println("----------------");
                        System.out.println(contact.toString());
                    }
                    break;

                case 2:
                    sc.nextLine();
                    System.out.println("Введите имя контакта");
                    String name = sc.nextLine();

                    System.out.println("Введите номер телефона контакта");
                    String phNumber = sc.nextLine();

                    System.out.println("Введите номер банковской карты контакта");
                    BigDecimal cardNumber = sc.nextBigDecimal();

                    sc.nextLine();
                    System.out.println("Введите заметку к контакту");
                    String note = sc.nextLine();

                    Contact contactToCreate = Contact.builder()
                            .name(name)
                            .phoneNumber(phNumber)
                            .bankCardNumber(cardNumber)
                            .note(note)
                            .build();

                    System.out.println(add(contactToCreate));
                    break;

                case 3:
                    System.out.println("Введите id контакта, который нужно обновить");
                    long id = sc.nextLong();

                    Contact contact = getById(id);

                    System.out.println("""
                            Выберите поле, которое нужно обновить
                            1. Имя контакта
                            2. Номер телефона
                            3. Номер банковской карты
                            4. Заметка о контакте
                            9. Выйти из меню обновления""");

                    int fieldChoice = sc.nextInt();

                    while (fieldChoice != 9) {
                        switch (fieldChoice) {
                            case 1:
                                sc.nextLine();
                                System.out.println("Введите новое имя");
                                String newName = sc.nextLine();
                                contact.setName(newName);
                                System.out.println("\n" + save(contact) + "\n");
                                break;

                            case 2:
                                sc.nextLine();
                                System.out.println("Введите новый номер телефона");
                                String newPhNumber = sc.nextLine();
                                contact.setPhoneNumber(newPhNumber);
                                System.out.println("\n" + save(contact) + "\n");
                                break;

                            case 3:
                                System.out.println("Введите новый номер банковской карты");
                                BigDecimal newCardNumber = sc.nextBigDecimal();
                                contact.setBankCardNumber(newCardNumber);
                                System.out.println("\n" + save(contact) + "\n");
                                break;

                            case 4:
                                sc.nextLine();
                                System.out.println("Введите новую заметку о контакте");
                                String newNote = sc.nextLine();
                                contact.setNote(newNote);
                                System.out.println("\n" + save(contact) + "\n");
                                break;
                        }
                        System.out.println("""
                                Выберите поле, которое нужно обновить
                                1. Имя контакта
                                2. Номер телефона
                                3. Номер банковской карты
                                4. Заметка о контакте
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
                    1. Вывести все контакты
                    2. Добавить новый контакт
                    3. Обновить существующий контакт
                    4. Удалить контакт по id
                    9. Выйти из меню Контактов""");

            choice = sc.nextInt();
        }

        System.out.println("Вы вышли из меню Контактов \n");
    }
}
