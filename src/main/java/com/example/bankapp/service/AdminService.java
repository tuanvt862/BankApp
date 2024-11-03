package com.example.bankapp.service;

import com.example.bankapp.dto.AccountDTO;
import com.example.bankapp.model.Account;

import java.util.List;

public interface AdminService {
    public Account UpdateAccount(AccountDTO accountDTO);
    public AccountDTO findAccountById(Long id);

    public List<Account> getAllAccounts();
    public void deleteAccountById(Long accountId);
    public Account searchByIdentificationNumber(String identificationNumber);
}
