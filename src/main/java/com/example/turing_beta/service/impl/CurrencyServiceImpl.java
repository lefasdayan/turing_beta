package com.example.turing_beta.service.impl;

import com.example.turing_beta.entity.Currency;
import com.example.turing_beta.exception.exceptions.common.AmountSetWrongException;
import com.example.turing_beta.exception.exceptions.currency.CurrencyAlreadyExistsException;
import com.example.turing_beta.exception.exceptions.currency.CurrencyFieldsEmptyException;
import com.example.turing_beta.exception.exceptions.currency.CurrencyNotFoundException;
import com.example.turing_beta.repos.CurrencyRepo;
import com.example.turing_beta.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepo currencyRepo;

    @Override
    public Currency save(Currency currency) {
        Currency currencyFromDb = getById(currency.getId()); //ловит existence / not found

        checkFieldsValidity(currency);
        currencyRepo.save(currency);
        return currency;
    }

    @Override
    public Currency add(Currency currency) {
        if (currency.getId() != null && currencyRepo.existsById(currency.getId())) {
            throw new CurrencyAlreadyExistsException(String.format("Currency with id %d already exists", currency.getId()));
        }
        checkFieldsValidity(currency);
        currencyRepo.save(currency);
        return currency;
    }

    @Override
    public Currency getByName(String name) {
        Optional<Currency> foundCurrency = currencyRepo.findByName(name);
        if (foundCurrency.isEmpty()) {
            throw new CurrencyNotFoundException(String.format("Could not find a currency with name %s", name));
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
            throw new CurrencyNotFoundException(String.format("Could not find a currency with id = %d", id));
        }
        return foundCurrency.get();
    }

    private void checkFieldsValidity(Currency currency) throws CurrencyFieldsEmptyException, AmountSetWrongException {
        if (!StringUtils.hasText(currency.getName())
                || currency.getCourseToRubble() == null) {
            throw new CurrencyFieldsEmptyException("Cannot save currency with empty field(s)");
        }

        if (currency.getCourseToRubble().compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new AmountSetWrongException("Cannot save currency with a course less than 0 or equal to 0");
        }
    }
}
