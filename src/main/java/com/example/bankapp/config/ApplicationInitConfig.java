package com.example.bankapp.config;

import com.example.bankapp.enums.Role;
import com.example.bankapp.model.Account;
import com.example.bankapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.HashSet;

@Configuration
public class ApplicationInitConfig {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner (AccountRepository accountRepository){
        return agrs->{
            if(accountRepository.findByUsername("admin").isEmpty())
            {
//                var roles = new HashSet<String>();
//                roles.add(Role.ADMIN.name());

                HashSet<String> roles = new HashSet<>();
//         roles.add(Role.USER.name());

                roles.add("ADMIN");
                Account account = new Account();
                account.setUsername("admin".trim());
                account.setPassword(passwordEncoder.encode("admin"));
                account.setRoles(roles);
                account.setBalance(BigDecimal.ZERO );
                 accountRepository.save(account);
            }
        };

    }
}
