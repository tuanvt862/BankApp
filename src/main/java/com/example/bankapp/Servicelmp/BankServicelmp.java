package com.example.bankapp.Servicelmp;

import com.example.bankapp.Mapper.AccountMapper;
import com.example.bankapp.model.Account;
import com.example.bankapp.model.Transaction;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.TransactionRepository;
import com.example.bankapp.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BankServicelmp implements BankService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

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
}
