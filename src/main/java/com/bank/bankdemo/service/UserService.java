package com.bank.bankdemo.service;

import com.bank.bankdemo.dto.BankResponse;
import com.bank.bankdemo.dto.EnquiryRequest;
import com.bank.bankdemo.dto.UserRequest;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
}
