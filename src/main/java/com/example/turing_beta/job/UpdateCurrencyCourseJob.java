package com.example.turing_beta.job;

import com.example.turing_beta.repos.CurrencyRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateCurrencyCourseJob implements Runnable {

    private final CurrencyRepo currencyRepo;

    @Override
    public void run() {

    }
}
