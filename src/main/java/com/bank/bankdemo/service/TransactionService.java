package com.bank.bankdemo.service;

import com.bank.bankdemo.dto.TransactionDto;
import com.bank.bankdemo.entity.Transaction;

public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);
}
