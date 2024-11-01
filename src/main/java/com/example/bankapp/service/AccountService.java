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

@Service
public class AccountService implements UserDetailsService {
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
    public Account searchByIdentificationNumber(String identificationNumber) {

        return accountRepository.findByIdentificationNumber(identificationNumber).orElseThrow(() -> new RuntimeException("Account not found"));

    }

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




     public void deposit (Account account, BigDecimal amount)
     {
         account.setBalance(account.getBalance().add(amount));
         accountRepository.save(account);

         Transaction transaction = new Transaction(
                          amount,
                          "Deposit",
                 account,
                 LocalDateTime.now()

                  );
         transactionRepository.save(transaction);
     }

     public void withdraw(Account account, BigDecimal amount){
         if(account.getBalance().compareTo(amount)<0){
             throw new RuntimeException("Insufficient funds");
         }
         account.setBalance(account.getBalance().subtract(amount));
         accountRepository.save(account);

         Transaction transaction = new Transaction(
                 amount,
                 "withdrawal",
                 account,
                 LocalDateTime.now()
         );
         transactionRepository.save(transaction);

     }


     public List<Transaction> getTransactionHistory(Account account){
         return transactionRepository.findByAccountId(account.getId());
     }


     @Override
     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
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


    public void transferAmount(Account fromAccount , String toUsername, BigDecimal  amount )
    {
        if(fromAccount.getBalance().compareTo(amount)<0)
        {
            throw new RuntimeException("Insufficients funds");

        }
        Account toAccount = accountRepository.findByUsername(toUsername.trim()).orElseThrow(()-> new RuntimeException("Reciepient account not found"));
        //Deduct
         fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
         accountRepository.save(fromAccount);

         //Add
        toAccount.setBalance((toAccount.getBalance().add(amount)));
        accountRepository.save(toAccount);

        // Create transaction record
        Transaction debitTransaction = new Transaction(
                        amount,
                        "Transfer Out to " + toAccount.getUsername(),
                fromAccount,
                LocalDateTime.now()
                );
                transactionRepository.save(debitTransaction);

        Transaction creditTransaction = new Transaction(
                amount,
                "Transfer in from " + fromAccount.getUsername(),
                toAccount,
                LocalDateTime.now()
        );
        transactionRepository.save(creditTransaction);

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
}
