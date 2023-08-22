package com.example.turing_beta.service;

import com.example.turing_beta.entity.Contact;

import java.util.List;

public interface ContactService {
    List<Contact> getAll();

    Contact add(Contact contact);

    Contact save(Contact contact);

    Contact delete(Contact contact);

    Contact getById(Long id);
    Contact getByName(String name);
}
