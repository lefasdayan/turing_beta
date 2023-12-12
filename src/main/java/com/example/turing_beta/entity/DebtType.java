package com.example.turing_beta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "debt_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebtType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "note", length = 240)
    private String note;
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "debtType")
    private List<Debt> debts;
}
