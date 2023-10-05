package com.example.turing_beta.repos;

import com.example.turing_beta.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepo extends JpaRepository<Currency, Long> {
    Optional<Currency> findByName(String name);
}
