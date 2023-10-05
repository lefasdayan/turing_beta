package com.example.turing_beta.repos;

import com.example.turing_beta.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
    @Query(value = "select * from account where name like '%:name%'", nativeQuery = true)
    Optional<Account> findByName(@Param("name") String name);
}
