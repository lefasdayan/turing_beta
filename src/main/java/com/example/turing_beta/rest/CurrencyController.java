package com.example.turing_beta.rest;

import com.example.turing_beta.entity.Currency;
import com.example.turing_beta.repos.CurrencyRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/currencies")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyRepo currencyRepo;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Currency>> getAllCurrencies(){
        return new ResponseEntity<>(currencyRepo.findAll(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Currency> getById(@PathVariable Long id){
        return new ResponseEntity<>(currencyRepo.findById(id).get(), HttpStatusCode.valueOf(200));
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> addNewCurrency(@RequestBody Currency currency){
        currencyRepo.save(currency);
        return ResponseEntity.ok().build();
    }
}
