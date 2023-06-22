package com.bank.bankdemo.service;

import com.bank.bankdemo.dto.*;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
    BankResponse transferFunds(TransferRequest request);
    }
