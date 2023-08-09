package com.example.turing_beta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", length = 50)
    private String name;
    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id")
    private Currency currency;
    @Column(name = "date_time", columnDefinition = "DATE")
    private LocalDateTime dateTime;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    private TransactionType type;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_acc_id")
    private Account fromAcc;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_acc_id")
    private Account toAcc;
    @ManyToMany
    @JoinTable(
            name = "debt_history",
            inverseJoinColumns = @JoinColumn(name = "debt_id"),
            joinColumns = @JoinColumn(name = "transact_id")
    )
    private List<Debt> debts;
}
