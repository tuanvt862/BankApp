package com.example.bankapp.model;
import jakarta.persistence.*;

import org.hibernate.type.descriptor.java.LocalDateTimeJavaType;
import org.springframework.security.core.GrantedAuthority;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private BigDecimal amount;
    private String type;
    private LocalDateTime timestamp;
    @ManyToOne
    @JoinColumn(name="account_id")
    private Account account;

    public Transaction() {
    }

    public Transaction(BigDecimal amount, String type, Account account, LocalDateTime timestamp) {
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
