package com.example.turing_beta.service.impl;

import com.example.turing_beta.entity.Currency;
import com.example.turing_beta.entity.DebtType;
import com.example.turing_beta.exception.exceptions.common.ObjectNotFoundException;
import com.example.turing_beta.repos.DebtTypeRepo;
import com.example.turing_beta.service.DebtTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class DebtTypeServiceImpl implements DebtTypeService {
    private final DebtTypeRepo debtTypeRepo;

    @Override
    public List<DebtType> getAll() {
        return debtTypeRepo.findAll();
    }

    @Override
    public DebtType getById(Long id) {
        Optional<DebtType> foundDebtType = debtTypeRepo.findById(id);
        if (foundDebtType.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Debt type with id = %d was not found", id));
        }
        return foundDebtType.get();
    }

    @Override
    public DebtType getByName(String name) {
        Optional<DebtType> foundDebtType = debtTypeRepo.findByName(name);
        if (foundDebtType.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Debt type with id = %s was not found", name));
        }
        return foundDebtType.get();
    }

    public void console() {
        Scanner sc = new Scanner(System.in);

        System.out.println("""
                Выберите действие:
                1. Вывести все типы долгов
                9. Выйти из меню Типов долгов""");

        int choice = sc.nextInt();

        while (choice != 9) {
            switch (choice) {
                case 1:
                    for (DebtType debtType : getAll()) {
                        System.out.println("----------------");
                        System.out.println(debtType.toString());
                    }
                    break;
            }
            System.out.println("""
                Выберите действие:
                1. Вывести все типы долгов
                9. Выйти из меню Типов долгов""");

            choice = sc.nextInt();
        }

        System.out.println("Вы вышли из меню Типов долгов \n");
    }
}
