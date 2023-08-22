package com.example.turing_beta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "contact")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", length = 140)
    private String name;
    @Column(name = "ph_number", length = 20)
    private String phoneNumber;
    @Column(name = "bank_card_number", precision = 16)
    private BigDecimal bankCardNumber;
    @Column(name = "note", length = 500)
    private String note;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "contact")
    private List<Debt> debts;
}
