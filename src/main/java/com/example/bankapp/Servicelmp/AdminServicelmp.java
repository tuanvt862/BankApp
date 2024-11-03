package com.example.bankapp.Servicelmp;

import com.example.bankapp.Mapper.AccountMapper;
import com.example.bankapp.dto.AccountDTO;
import com.example.bankapp.model.Account;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.TransactionRepository;
import com.example.bankapp.service.AdminService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServicelmp implements AdminService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public AccountDTO findAccountById(Long id)
    {
        Account account =accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found") );
        AccountDTO accountDTO = accountMapper.toDTO(account);

        return accountDTO;
    }

    public List<Account> getAllAccounts()
    {
        return accountRepository.findAll();
    }
    public Account UpdateAccount(AccountDTO accountDTO)
    {
        if(!accountDTO.getPassword().equals(accountDTO.getRepeatpassword()))
        {
            throw new RuntimeException("password invalid");
        }
        if(!accountRepository.findByUsername(accountDTO.getUsername()).isPresent())
        {
            throw new RuntimeException("user does not exist");
        }
        Optional<Account> optionalAccount =accountRepository.findById(accountDTO.getId());
        if (!optionalAccount.isPresent()) {
            throw new EntityNotFoundException("Account not found with id: " + accountDTO.getId());
        }

        Account updatedAccount = accountMapper.toEntity(accountDTO, optionalAccount.get());
        return accountRepository.save(updatedAccount);
    }
    @Transactional
    public void deleteAccountById(Long accountId) {
        // Lấy account từ ID
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Xóa tất cả các transaction liên quan đến account này
        transactionRepository.deleteByAccount(account);

        // Sau đó xóa account
        accountRepository.delete(account);
    }

    public Account searchByIdentificationNumber(String identificationNumber) {

        return accountRepository.findByIdentificationNumber(identificationNumber).orElseThrow(() -> new RuntimeException("Account not found"));

    }
}
