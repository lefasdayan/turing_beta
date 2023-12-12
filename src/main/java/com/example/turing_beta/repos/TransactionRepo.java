package com.example.turing_beta.repos;

import com.example.turing_beta.entity.Debt;
import com.example.turing_beta.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByDebtsId(Long debtsId);
}
