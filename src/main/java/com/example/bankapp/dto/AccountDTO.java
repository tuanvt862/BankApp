package com.example.bankapp.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class AccountDTO {
    private Long id;

    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotEmpty(message = "Repeat password cannot be empty")
    private String repeatpassword;

    @NotEmpty(message = "Identification number cannot be empty")
    @Pattern(regexp = "^[0-9]{9,12}$", message = "Identification number must be between 9 and 12 digits")
    private String identificationNumber;

    @NotEmpty(message = "Phone number cannot be empty")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be between 10 and 15 digits")
    private String phoneNumber;

    @NotNull(message = "Birthdate cannot be null")
    @Past(message = "Birthdate must be in the past")
    @DateTimeFormat(pattern = "dd/MM/yyyy") // Định dạng dd/MM/yyyy
    private LocalDate birthdate;

    public AccountDTO() {
    }

    public AccountDTO(Long id, String username, String password, String repeatpassword, String identificationNumber, String phoneNumber, LocalDate birthdate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.repeatpassword = repeatpassword;
        this.identificationNumber = identificationNumber;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber( String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public  String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber( String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public  LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate( LocalDate birthdate) {
        this.birthdate = birthdate;
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

    public String getRepeatpassword() {
        return repeatpassword;
    }

    public void setRepeatpassword(String repeatpassword) {
        this.repeatpassword = repeatpassword;
    }
}
