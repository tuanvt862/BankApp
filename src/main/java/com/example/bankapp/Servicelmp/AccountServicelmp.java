package com.example.bankapp.Servicelmp;


import com.example.bankapp.Mapper.AccountMapper;
import com.example.bankapp.dto.AccountDTO;
import com.example.bankapp.model.Account;
import com.example.bankapp.model.Transaction;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.TransactionRepository;

import com.example.bankapp.service.AccountService;
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

@Service
public class AccountServicelmp implements UserDetailsService, AccountService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    public Account findAccountByUsername(String username)
    {
        return accountRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Account not found") );
    }

    public Account registerAccount(AccountDTO accountDTO)
    {
        if(accountRepository.findByUsername(accountDTO.getUsername()).isPresent())
        {
            throw new RuntimeException("Username already");
        }
        if(!accountDTO.getPassword().equals(accountDTO.getRepeatpassword())){

            throw new RuntimeException("password invalid");

        }
        if(accountRepository.findByIdentificationNumber(accountDTO.getIdentificationNumber()).isPresent()){

            throw new RuntimeException("IdentificationNumber already");

        }
        HashSet<String> roles = new HashSet<>();
//         roles.add(Role.USER.name());
        roles.add("USER");
        Account account = new Account();
        account.setUsername(accountDTO.getUsername().trim());
        account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        account.setBirthdate(accountDTO.getBirthdate());
        account.setIdentificationNumber(accountDTO.getIdentificationNumber());
        account.setPhoneNumber(accountDTO.getPhoneNumber());
        account.setRoles(roles);
        account.setBalance(BigDecimal.ZERO );
        return accountRepository.save(account);
    }

    public boolean changePassword(AccountDTO accountDTO) {
        if(!accountRepository.findByUsername(accountDTO.getUsername()).isPresent())
        {
            throw new RuntimeException("user does not exist");
        }
        Optional<Account> account = accountRepository.findByUsername(accountDTO.getUsername());
        if (account.isPresent() && passwordEncoder.matches(accountDTO.getRepeatpassword(), account.get().getPassword())&& account.get().getIdentificationNumber().equals(accountDTO.getIdentificationNumber())&& account.get().getPhoneNumber().equals(accountDTO.getPhoneNumber())&& account.get().getBirthdate().equals(accountDTO.getBirthdate())) {
            Account updatedAccount = accountMapper.toEntity(accountDTO, account.get());
            accountRepository.save(updatedAccount);
            return true;
        } else {
            throw new RuntimeException("information does not match");
        }
    }

    public boolean forgotPassword(AccountDTO accountDTO) {
        if(!accountRepository.findByUsername(accountDTO.getUsername()).isPresent())
        {
            throw new RuntimeException("user does not exist");
        }
        Optional<Account> account = accountRepository.findByUsername(accountDTO.getUsername());
        if (account.isPresent() && accountDTO.getPassword().equals(accountDTO.getRepeatpassword())&& account.get().getIdentificationNumber().equals(accountDTO.getIdentificationNumber())&& account.get().getPhoneNumber().equals(accountDTO.getPhoneNumber())&& account.get().getBirthdate().equals(accountDTO.getBirthdate())) {
            Account updatedAccount = accountMapper.toEntity(accountDTO, account.get());
            accountRepository.save(updatedAccount);
            return true;
        } else {
            throw new RuntimeException("Password invalid");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = findAccountByUsername(username);
        if(account == null)
        {
            throw new UsernameNotFoundException("Username or Password not found");
        }
        return new Account(
                account.getUsername(),
                account.getPassword(),
                account.getBalance(),
                account.getIdentificationNumber(),
                account.getPhoneNumber(),
                account.getBirthdate(),
                account.getTransaction(),
                authorities(),
                account.getRoles()
        );

    }
    public Collection<? extends GrantedAuthority> authorities(){
        return Arrays.asList(new SimpleGrantedAuthority("User"));
    }
}
