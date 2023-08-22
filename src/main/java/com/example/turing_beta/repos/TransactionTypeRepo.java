package com.example.turing_beta.repos;

import com.example.turing_beta.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionTypeRepo extends JpaRepository<TransactionType, Long> {
    boolean existsByName(String name);
    Optional<TransactionType> findByName(String name);
}
