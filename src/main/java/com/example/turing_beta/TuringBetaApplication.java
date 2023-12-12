package com.example.turing_beta;

import com.example.turing_beta.service.impl.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.text.ParseException;
import java.util.Scanner;

@SpringBootApplication
public class TuringBetaApplication {
    public static void main(String[] args) throws ParseException {
        ConfigurableApplicationContext context = SpringApplication.run(TuringBetaApplication.class, args);
        AccountServiceImpl accountService = context.getBean(AccountServiceImpl.class);
        ContactServiceImpl contactService = context.getBean(ContactServiceImpl.class);
        CurrencyServiceImpl currencyService = context.getBean(CurrencyServiceImpl.class);
        TransactionServiceImpl transactionService = context.getBean(TransactionServiceImpl.class);
        DebtServiceImpl debtService = context.getBean(DebtServiceImpl.class);
        DebtTypeServiceImpl debtTypeService = context.getBean(DebtTypeServiceImpl.class);
        TransactionTypeServiceImpl transactionTypeService = context.getBean(TransactionTypeServiceImpl.class);


        System.out.println("""
                Выберите сущность, с которой хотите работать:
                1. Счета
                2. Контакты
                3. Валюты
                4. Транзакции
                5. Долги
                6. Типы долгов
                7. Типы транзакций""");

        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        while (choice != 9) {
            switch (choice) {
                case 1:
                    accountService.console();
                    break;
                case 2:
                    contactService.console();
                    break;
                case 3:
                    currencyService.console();
                    break;
                case 4:
                    transactionService.console();
                    break;
                case 5:
                    debtService.console();
                    break;
                case 6:
                    debtTypeService.console();
                    break;
                case 7:
                    transactionTypeService.console();
                    break;
            }

            System.out.println("""
                Выберите сущность, с которой хотите работать:
                1. Счета
                2. Контакты
                3. Валюты
                4. Транзакции
                5. Долги
                6. Типы долгов
                7. Типы транзакций""");
            choice = sc.nextInt();
        }
    }


}
