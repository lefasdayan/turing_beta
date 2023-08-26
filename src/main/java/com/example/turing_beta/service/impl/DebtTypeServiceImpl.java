package com.example.turing_beta.service.impl;

import com.example.turing_beta.entity.DebtType;
import com.example.turing_beta.exception.exceptions.common.ObjectNotFoundException;
import com.example.turing_beta.repos.DebtTypeRepo;
import com.example.turing_beta.service.DebtTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DebtTypeServiceImpl implements DebtTypeService {
    private final DebtTypeRepo debtTypeRepo;

    @Override
    public List<DebtType> getAll() {
        return debtTypeRepo.findAll();
    }

    @Override
    public DebtType getById(Long id) {
        Optional<DebtType> foundDebtType = debtTypeRepo.findById(id);
        if (foundDebtType.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Debt type with id = %d was not found", id));
        }
        return foundDebtType.get();
    }

    @Override
    public DebtType getByName(String name) {
        Optional<DebtType> foundDebtType = debtTypeRepo.findByName(name);
        if (foundDebtType.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Debt type with id = %s was not found", name));
        }
        return foundDebtType.get();
    }
}
