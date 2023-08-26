package com.example.turing_beta.service;

import com.example.turing_beta.entity.Currency;

import java.util.List;

public interface CurrencyService {

    Currency getByName(String name);

    List<Currency> getAll();

    Currency getById(Long id);
}
