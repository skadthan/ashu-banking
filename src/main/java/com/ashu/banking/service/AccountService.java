package com.ashu.banking.service;

import java.security.Principal;

import com.ashu.banking.domain.PrimaryAccount;
import com.ashu.banking.domain.SavingsAccount;

public interface AccountService {
	
    PrimaryAccount createPrimaryAccount();
    
    SavingsAccount createSavingsAccount();
    
    void deposit(String accountType, double amount, Principal principal);
    
    void withdraw(String accountType, double amount, Principal principal);
    
}
