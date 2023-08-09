package com.example.turing_beta.repos;

import com.example.turing_beta.entity.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtRepo extends JpaRepository<Debt, Long> {
}
