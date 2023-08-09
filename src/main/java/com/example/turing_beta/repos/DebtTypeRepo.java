package com.example.turing_beta.repos;

import com.example.turing_beta.entity.DebtType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtTypeRepo extends JpaRepository<DebtType, Long> {
}
