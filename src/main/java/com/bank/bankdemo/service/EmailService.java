package com.bank.bankdemo.service;

import com.bank.bankdemo.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
