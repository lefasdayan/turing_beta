package com.example.turing_beta.rest.dto.currency;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AllFieldsCurrency {
    private Long id;
    private String name;
    private BigDecimal courseToRubble;
}
