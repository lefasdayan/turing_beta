package com.example.turing_beta.service.impl;

import com.example.turing_beta.entity.Debt;
import com.example.turing_beta.exception.exceptions.common.AmountSetWrongException;
import com.example.turing_beta.exception.exceptions.common.ObjectAlreadyExistsException;
import com.example.turing_beta.exception.exceptions.common.ObjectFieldsEmptyException;
import com.example.turing_beta.exception.exceptions.common.ObjectNotFoundException;
import com.example.turing_beta.exception.exceptions.debt.DebtWrongDueDateException;
import com.example.turing_beta.repos.DebtRepo;
import com.example.turing_beta.service.DebtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
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
            throw new ObjectAlreadyExistsException(String.format("Debt with id = %d already exists", debt.getId()));
        }
        checkFieldsValidity(debt);

        debt = debtRepo.save(debt);
        return debt;
    }

    @Override
    public Debt getById(Long id) {
        Optional<Debt> foundDebt = debtRepo.findById(id);
        if (foundDebt.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Cannot find debt with id = %d", id));
        }
        return foundDebt.get();
    }

    @Override
    public Debt save(Debt debt) {
        Debt debtFromDb = getById(debt.getId());
        checkFieldsValidity(debt);
        debt = debtRepo.save(debt);
        return debt;
    }

    @Override
    public void deleteById(Long id) {
        Debt debt = getById(id);
        debtRepo.deleteById(id);
    }

    @Override
    public List<Debt> getAllByContactId(Long id) {
        return debtRepo.findAllByContactId(id); //todo найти все долги какого-то контакта по его id
    }

    private void checkFieldsValidity(Debt debt) throws ObjectFieldsEmptyException, AmountSetWrongException,
            DebtWrongDueDateException, ObjectAlreadyExistsException {
        if (!StringUtils.hasText(debt.getName())
                || debt.getAmount() == null
                || debt.getCurrency() == null) {
            throw new ObjectFieldsEmptyException("Cannot add debt with empty field(s)");
        }
        if (debt.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new AmountSetWrongException("Cannot add debt with amount less than or equal to 0");
        }
        if (debt.getDateDue().isBefore(debt.getDateStart())) {
            throw new DebtWrongDueDateException("Cannot add debt with due date earlier than date of start");
        }
        if (debtRepo.findByName(debt.getName()).isPresent()
                && !Objects.equals(debtRepo.findByName(debt.getName()).get().getId(), debt.getId())){
            throw new ObjectAlreadyExistsException(String.format("Cannot save. " +
                    "Debt with name %s already exists", debt.getName()));
        }
    }
}
