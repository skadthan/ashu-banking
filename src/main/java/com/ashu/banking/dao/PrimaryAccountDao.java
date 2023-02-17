package com.ashu.banking.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ashu.banking.domain.PrimaryAccount;

public interface PrimaryAccountDao extends CrudRepository<PrimaryAccount,Long> {

    List<PrimaryAccount> findByAccountNumber (int accountNumber);
}
