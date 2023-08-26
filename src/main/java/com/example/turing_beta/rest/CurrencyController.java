package com.example.turing_beta.rest;

import com.example.turing_beta.entity.Currency;
import com.example.turing_beta.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/currency")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Currency> getById(@PathVariable Long id) {
        return ResponseEntity.ok(currencyService.getById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Currency> getByName(@PathVariable String name) {
        return ResponseEntity.ok(currencyService.getByName(name));
    }

//    @PostMapping
//    public ResponseEntity<Currency> addCurrency(@RequestBody Currency currency) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(currencyService.add(currency));
//    }
}
