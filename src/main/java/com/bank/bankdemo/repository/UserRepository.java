package com.bank.bankdemo.repository;

import com.bank.bankdemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.StyledEditorKit;

public interface UserRepository extends JpaRepository <User, Long> {
    Boolean existsByEmail (String email);
    Boolean existsByAccountNumber (String accountNumber);
    User findByAccountNumber(String accountNumber);
}
