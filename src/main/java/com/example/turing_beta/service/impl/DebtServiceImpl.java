package com.example.turing_beta.service.impl;

import com.example.turing_beta.entity.Debt;
import com.example.turing_beta.exception.exceptions.common.AmountSetWrongException;
import com.example.turing_beta.exception.exceptions.debt.DebtAlreadyExistsException;
import com.example.turing_beta.exception.exceptions.debt.DebtFieldsEmptyException;
import com.example.turing_beta.exception.exceptions.debt.DebtNotFoundException;
import com.example.turing_beta.exception.exceptions.debt.DebtWrongDueDateException;
import com.example.turing_beta.repos.DebtRepo;
import com.example.turing_beta.service.DebtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DebtServiceImpl implements DebtService {
    private final DebtRepo debtRepo;

    @Override
    public List<Debt> getAll() {
        return debtRepo.findAll();
    }

    @Override
    public Debt add(Debt debt) {
        if (debt.getId() != null && debtRepo.existsById(debt.getId())) {
            throw new DebtAlreadyExistsException(String.format("Debt with id = %d already exists", debt.getId()));
        }
        checkFieldsValidity(debt);

        debtRepo.save(debt);
        return debt;
    }

    @Override
    public Debt getById(Long id) {
        Optional<Debt> foundDebt = debtRepo.findById(id);
        if (foundDebt.isEmpty()) {
            throw new DebtNotFoundException(String.format("Cannot find debt with id = %d", id));
        }
        return foundDebt.get();
    }

    @Override
    public Debt save(Debt debt) {
        Debt debtFromDb = getById(debt.getId());
        checkFieldsValidity(debt);

        debtRepo.save(debt);
        return debt;
    }

    @Override
    public Debt delete(Debt debt) {
        if (debt.getId() != null && !debtRepo.existsById(debt.getId())) {
            throw new DebtNotFoundException(String.format("Cannot find debt with id = %d", debt.getId()));
        }
        debtRepo.delete(debt);
        return debt;
    }

    @Override
    public List<Debt> getAllByContactName(String name) {
        return debtRepo.findAllByContactName(name); //todo найти все долги какого-то контакта по его имени
    }

    private void checkFieldsValidity(Debt debt) throws DebtFieldsEmptyException, AmountSetWrongException, DebtWrongDueDateException {
        if (!StringUtils.hasText(debt.getName())
                || debt.getAmount() == null
                || debt.getCurrency() == null) {
            throw new DebtFieldsEmptyException("Cannot add debt with empty field(s)");
        }
        if (debt.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new AmountSetWrongException("Cannot add debt with amount less than or equal to 0");
        }
        if (debt.getDateDue().isBefore(debt.getDateStart())) {
            throw new DebtWrongDueDateException("Cannot add debt with due date earlier than date of start");
        }
    }
}
