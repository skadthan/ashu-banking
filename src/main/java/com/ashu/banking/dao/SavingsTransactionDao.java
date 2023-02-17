package com.ashu.banking.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ashu.banking.domain.SavingsTransaction;

public interface SavingsTransactionDao extends CrudRepository<SavingsTransaction, Long> {

    List<SavingsTransaction> findAll();
}

