package com.example.turing_beta.service.impl;

import com.example.turing_beta.entity.Contact;
import com.example.turing_beta.exception.exceptions.common.ObjectAlreadyExistsException;
import com.example.turing_beta.exception.exceptions.common.ObjectFieldsEmptyException;
import com.example.turing_beta.exception.exceptions.common.ObjectNotFoundException;
import com.example.turing_beta.repos.ContactRepo;
import com.example.turing_beta.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactRepo contactRepo;

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contact add(Contact contact) {
        if (contact.getId() != null && contactRepo.existsById(contact.getId())) {
            throw new ObjectAlreadyExistsException(String.format("Contact with id %d already exists", contact.getId()));
        }
        if (!StringUtils.hasText(contact.getName())) {
            throw new ObjectFieldsEmptyException("Cannot add contact with empty field(s)");
        }
        contact = contactRepo.save(contact);
        return contact;
    }

    @Override
    public Contact save(Contact contact) {
        if (contactRepo.findByName(contact.getName()).isPresent()
                && !Objects.equals(contactRepo.findByName(contact.getName()).get().getId(), contact.getId())){ //todo перенести в метод checkFieldsValidity
            throw new ObjectAlreadyExistsException(String.format("Cannot save. " +
                    "Contact with name %s already exists", contact.getName()));
        }
        Contact contactFromDb = getById(contact.getId());
        if (!StringUtils.hasText(contact.getName())) {
            throw new ObjectFieldsEmptyException("Cannot add contact with empty field(s)");
        }
        contact = contactRepo.save(contact);
        return contact;
    }

    @Override
    public void deleteById(Long id) {
        Contact contact = getById(id);
        contactRepo.deleteById(id);
    }

    @Override
    public Contact getById(Long id) {
        Optional<Contact> foundContact = contactRepo.findById(id);
        if (foundContact.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Cannot find contact with id = %d", id));
        }
        return foundContact.get();
    }

    @Override
    public Contact getByName(String name) {
        Optional<Contact> foundContact = contactRepo.findByName(name);
        if (foundContact.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Cannot find contact with name %s", name));
        }

        return foundContact.get();
    }
}
