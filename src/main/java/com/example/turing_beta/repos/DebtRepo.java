package com.example.turing_beta.repos;

import com.example.turing_beta.entity.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DebtRepo extends JpaRepository<Debt, Long> {
    // todo @Query("select debt.name, debt.amount from debt join contact c on c.id = debt.contact_id where c.name like '%?%'")
    List<Debt> findAllByContactId(Long id);

    Optional<Debt> findByName(String name);
}
