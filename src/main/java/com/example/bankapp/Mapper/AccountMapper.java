package com.example.bankapp.Mapper;

import com.example.bankapp.dto.AccountDTO;
import com.example.bankapp.model.Account;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
@Component
public class AccountMapper {

    private final PasswordEncoder passwordEncoder;

    public AccountMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Phương thức chuyển từ DTO sang Entity
    public Account toEntity(AccountDTO accountDTO, Account existingAccount) {
        Account account = new Account();
        account.setId(existingAccount.getId());
        account.setUsername(accountDTO.getUsername().trim());
        account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        account.setBirthdate(accountDTO.getBirthdate());
        account.setIdentificationNumber(accountDTO.getIdentificationNumber());
        account.setPhoneNumber(accountDTO.getPhoneNumber());
        account.setBalance(existingAccount.getBalance());
        account.setRoles(new HashSet<>(List.of("USER"))); // Set roles cho Entity
        return account;
    }

    // Phương thức chuyển từ Entity sang DTO (nếu cần)
    public AccountDTO toDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setUsername(account.getUsername());
        accountDTO.setPassword(account.getPassword());
        accountDTO.setBirthdate(account.getBirthdate());
        accountDTO.setPhoneNumber((account.getPhoneNumber()));
        accountDTO.setIdentificationNumber(account.getIdentificationNumber());

        // Thêm các thuộc tính cần thiết khác từ Entity vào DTO nếu có
        return accountDTO;
    }
}

