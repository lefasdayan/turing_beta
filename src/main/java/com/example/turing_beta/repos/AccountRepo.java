package com.example.turing_beta.repos;

import com.example.turing_beta.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
    //@Query(value = "select * from account where name like '%:name%'", nativeQuery = true)
    //@Param("name")
    Optional<Account> findByName(String name);
}
