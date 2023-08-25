package com.example.turing_beta.rest;

import com.example.turing_beta.entity.Debt;
import com.example.turing_beta.service.DebtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/debt")
@RequiredArgsConstructor
public class DebtController {
    private final DebtService debtService;

    @GetMapping
    public ResponseEntity<List<Debt>> getAllDebts() {
        return ResponseEntity.ok(debtService.getAll());
    }

    @PostMapping
    public ResponseEntity<Debt> addDebt(@RequestBody Debt debt) {
        return ResponseEntity.status(HttpStatus.CREATED).body(debtService.add(debt));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Debt> getDebtById(@PathVariable Long id) {
        return ResponseEntity.ok(debtService.getById(id));
    }

    @PatchMapping
    public ResponseEntity<Debt> updateDebt(@RequestBody Debt debt) {
        return ResponseEntity.ok(debtService.save(debt));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDebt(@PathVariable Long id) {
        debtService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // todo getAllByContactId
}
