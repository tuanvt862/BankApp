package com.example.bankapp.service;

import com.example.bankapp.Mapper.AccountMapper;
import com.example.bankapp.dto.AccountDTO;
import com.example.bankapp.model.Account;
import com.example.bankapp.model.Transaction;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.TransactionRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


public interface AccountService {

    public Account registerAccount(AccountDTO accountDTO);

    public boolean changePassword(AccountDTO accountDTO);

    public boolean forgotPassword(AccountDTO accountDTO);

    public Collection<? extends GrantedAuthority> authorities();

    Account findAccountByUsername(String username);
}
