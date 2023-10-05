package com.example.turing_beta.rest.dto.account;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class NewAccountResponse {
    private Long id;
    private String name;
    private BigDecimal amount;
    private String currencyName;
}
