package com.example.bankapp.service;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.Transaction;
import org.springframework.security.core.GrantedAuthority;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface BankService {
    public void deposit (Account account, BigDecimal amount);

    public void withdraw(Account account, BigDecimal amount);

    public List<Transaction> getTransactionHistory(Account account);

    public void transferAmount(Account fromAccount , String toUsername, BigDecimal  amount );
}
