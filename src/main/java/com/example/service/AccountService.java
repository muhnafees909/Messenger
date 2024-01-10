package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public ResponseEntity<?> addUser(Account newAccount) {
        if(newAccount.getUsername() == null
        || newAccount.getUsername() == ""
        || newAccount.getPassword().length() < 4) {
            return ResponseEntity.badRequest().body("Invalid Username or Password");
        }
        if(accountRepository.findByUsername(newAccount.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username Already Exists");
        }
        return ResponseEntity.ok(accountRepository.save(newAccount));
    }

    public ResponseEntity<?> login(Account account) {
        Optional<Account> optionalAccount = accountRepository.findByUsername(account.getUsername());
        
        if(optionalAccount.isPresent()) {
            Account oAccount = optionalAccount.get();
            if(account.getPassword().equals(oAccount.getPassword())) {
                ObjectMapper objectMapper = new ObjectMapper();
                String accountJson;
                try {
                    accountJson = objectMapper.writeValueAsString(oAccount);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
                }
    
                // Return a JSON response for a successful login
                return ResponseEntity.status(HttpStatus.OK).body(accountJson);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Unsuccessful");

    }
}
