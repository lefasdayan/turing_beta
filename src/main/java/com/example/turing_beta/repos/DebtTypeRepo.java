package com.example.turing_beta.repos;

import com.example.turing_beta.entity.DebtType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DebtTypeRepo extends JpaRepository<DebtType, Long> {

    Optional<DebtType> findByName(String name);
}
