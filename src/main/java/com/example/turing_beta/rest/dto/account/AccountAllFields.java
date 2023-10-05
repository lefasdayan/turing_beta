package com.example.turing_beta.rest.dto.account;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountAllFields { //todo занести в сваггер
    private Long id;
    private String name;
    private BigDecimal amount;
    private String currencyName;
    private String bankName;
}
