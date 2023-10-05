package com.example.turing_beta.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "currency")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", length = 70, nullable = false)
    private String name;
    @Column(name = "course_to_rubble", precision = 12, scale = 7, nullable = false, unique = true)
    private BigDecimal courseToRubble;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "currency")
    @ToString.Exclude
    private List<Account> accounts;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "currency")
    @ToString.Exclude
    private List<Debt> debts;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "currency")
    @ToString.Exclude
    private List<Transaction> transactions;
}
