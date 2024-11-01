package com.example.bankapp.controller;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.Transaction;
import com.example.bankapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class BankController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        if(username.equals("admin"))
        {
            return "redirect:/admin/accounts";
        }
        model.addAttribute("account", account);
        return "dashboard";
    }



    @PostMapping("/deposit")

    public String deposit(@RequestParam BigDecimal amount) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        accountService.deposit(account,amount);
        return "redirect:/dashboard";
    }
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam BigDecimal amount ,Model model){
        String username =SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        try{
            accountService.withdraw(account, amount);
        }catch ( RuntimeException e)
        {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("account", account);
            return "dashboard";
        }
        return "redirect:/dashboard";
    }
    @GetMapping("/transactions")
    public String transactionHistory (Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        List<Transaction> transactions =  accountService.getTransactionHistory(account);
        model.addAttribute("transactions",transactions);
        return "transactions";
    }
    @PostMapping("/transfer")
    public String transferAmount (@RequestParam  String toUsername, @RequestParam BigDecimal amount, Model model)
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account fromAccount = accountService.findAccountByUsername(username);
        try{
            accountService.transferAmount(fromAccount , toUsername, amount);
        }catch(RuntimeException e){
            model.addAttribute("error", e.getMessage());
            model.addAttribute("account", fromAccount);
            return "dashboard";
        }
        return "redirect:/dashboard";
    }
}

