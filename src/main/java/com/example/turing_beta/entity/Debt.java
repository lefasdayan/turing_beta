package com.example.turing_beta.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "debt")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id")
    private Currency currency;
    @Column(name = "date_start", columnDefinition = "DATE")
    private LocalDateTime dateStart;
    @Column(name = "date_due", columnDefinition = "DATE")
    private LocalDateTime dateDue;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contact_id")
    private Contact contact;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    private DebtType debtType;
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "debt_history",
            inverseJoinColumns = @JoinColumn(name = "transact_id"),
            joinColumns = @JoinColumn(name = "debt_id")
    )
    private List<Transaction> transactions;

    public List<Transaction> addTransaction(Transaction transaction){
        this.transactions.add(transaction);
        transaction.getDebts().add(this);

        return this.transactions;
    }

    public List<Transaction> deleteTransaction(Transaction transaction){
        this.transactions.add(transaction);
        transaction.getDebts().remove(this);

        return  this.transactions;
    }
}
