package com.example.turing_beta.rest;

import com.example.turing_beta.entity.Contact;
import com.example.turing_beta.service.impl.ContactServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contact")
@RequiredArgsConstructor
public class ContactController {
    private final ContactServiceImpl contactService;

    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts(){
        return ResponseEntity.ok(contactService.getAll());
    }

    @PostMapping
    public ResponseEntity<Contact> addContact(@RequestBody Contact contact){
        return ResponseEntity.status(HttpStatus.CREATED).body(contactService.add(contact));
    }

    @PatchMapping
    public ResponseEntity<Contact> updateContact(@RequestBody Contact contact){
        return ResponseEntity.ok(contactService.save(contact));
    }

    @DeleteMapping
    public ResponseEntity<Contact> deleteContact(@RequestBody Contact contact){
        return ResponseEntity.ok(contactService.delete(contact));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id){
        return ResponseEntity.ok(contactService.getById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Contact> getContactByName(@PathVariable String name){
        return ResponseEntity.ok(contactService.getByName(name));
    }
}
