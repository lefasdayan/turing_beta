package com.example.turing_beta.repos;

import com.example.turing_beta.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepo extends JpaRepository<Contact, Long> {
    Optional<Contact> findByName(String name);
}
