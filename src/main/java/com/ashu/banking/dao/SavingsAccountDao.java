package com.ashu.banking.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ashu.banking.domain.SavingsAccount;

public interface SavingsAccountDao extends CrudRepository<SavingsAccount, Long> {

    List<SavingsAccount> findByAccountNumber (int accountNumber);
}
