package com.example.turing_beta.rest;

import com.example.turing_beta.entity.DebtType;
import com.example.turing_beta.service.impl.DebtTypeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/debt_type")
@RequiredArgsConstructor
public class DebtTypeController {
    private final DebtTypeServiceImpl debtTypeService;

    @GetMapping
    public ResponseEntity<List<DebtType>> getAllDebtTypes() {
        return ResponseEntity.ok(debtTypeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DebtType> getDebtTypeById(@PathVariable Long id) {
        return ResponseEntity.ok(debtTypeService.getById(id));
    }

    @GetMapping("/name/{name}") //для дропдауна. скорее всего, nameLike
    public ResponseEntity<DebtType> getDebtTypeByName(@PathVariable String name) {
        return ResponseEntity.ok(debtTypeService.getByName(name));
    }
}
