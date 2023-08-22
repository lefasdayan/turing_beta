package com.example.turing_beta.service;

import com.example.turing_beta.entity.DebtType;

import java.util.List;

public interface DebtTypeService {
    List<DebtType> getAll();
    DebtType getById(Long id);
    DebtType getByName(String name);
}