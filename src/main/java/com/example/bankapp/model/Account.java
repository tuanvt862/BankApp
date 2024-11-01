package com.example.bankapp.model;

import jakarta.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private BigDecimal balance;
    private String identificationNumber;
    private String phoneNumber;
    private LocalDate birthdate;

//    private Set<String> roles;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions ;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "account_roles", joinColumns = @JoinColumn(name = "account_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    // Getters và Setters

    // Phương thức UserDetails yêu cầu
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
    public Account() {
    }

//    public Account(String username, String password, BigDecimal balance,Set<String> roles, List<Transaction> transactions, Collection<? extends GrantedAuthority> authorities) {
//        this.username = username;
//        this.password = password;
//        this.balance = balance;
//       this.roles = roles;
//        this.transactions = transactions;
//        this.authorities = authorities;
//    }


    public Account(String username, String password, BigDecimal balance, String identificationNumber, String phoneNumber, LocalDate birthdate, List<Transaction> transactions, Collection<? extends GrantedAuthority> authorities, Set<String> roles) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.identificationNumber = identificationNumber;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
        this.transactions = transactions;
        this.authorities = authorities;
        this.roles = roles;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransaction() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }



//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
