package com.bank.bankdemo.repository;

import com.bank.bankdemo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository <Transaction, Long> {
}
