package com.example.turing_beta.service.impl;

import com.example.turing_beta.entity.Currency;
import com.example.turing_beta.exception.exceptions.common.ObjectNotFoundException;
import com.example.turing_beta.repos.CurrencyRepo;
import com.example.turing_beta.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepo currencyRepo;

    @Override
    public Currency getByName(String name) {
        Optional<Currency> foundCurrency = currencyRepo.findByName(name);
        if (foundCurrency.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Could not find a currency with name %s", name));
        }
        return foundCurrency.get();
    }

    @Override
    public List<Currency> getAll() {
        return currencyRepo.findAll();
    }

    @Override
    public Currency getById(Long id) {
        Optional<Currency> foundCurrency = currencyRepo.findById(id);
        if (foundCurrency.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Could not find a currency with id = %d", id));
        }
        return foundCurrency.get();
    }
}
