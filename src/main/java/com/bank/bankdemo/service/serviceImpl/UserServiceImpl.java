package com.bank.bankdemo.service.serviceImpl;

import com.bank.bankdemo.dto.*;
import com.bank.bankdemo.entity.User;
import com.bank.bankdemo.repository.TransactionRepository;
import com.bank.bankdemo.repository.UserRepository;
import com.bank.bankdemo.service.EmailService;
import com.bank.bankdemo.service.TransactionService;
import com.bank.bankdemo.service.UserService;
import com.bank.bankdemo.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TransactionService transactionService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativeNumber(userRequest.getAlternativeNumber())
                .status("ACTIVE")
                .build();

        User savedUser = userRepository.save(newUser);

        //Send email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Creation")
                .messageBody("Dear " + userRequest.getFirstName() + ", your account has been created successfully.\nYour Account Details: \n" +
                        "Account Name: " + savedUser.getFirstName() + " " + savedUser.getOtherName() + " " + savedUser.getLastName() + "\nAccount Number: " + savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(savedUser.getFirstName() + " " + savedUser.getOtherName() + " " + savedUser.getLastName())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountBalance(savedUser.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        //check if th account number exist in the db
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(foundUser.getFirstName() + " " + foundUser.getOtherName() + " " + foundUser.getLastName())
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(foundUser.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getOtherName() + " " + foundUser.getLastName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        //Check if the account exist
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName()+" "+userToCredit.getOtherName()+" "+userToCredit.getLastName())
                        .accountNumber(userToCredit.getAccountNumber())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        //Check if account exist
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        //check if the amount you intend to withdraw is not more than the current account balance
        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();
        if(availableBalance.intValue() < debitAmount.intValue()){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userToDebit.getFirstName() +" "+ userToDebit.getOtherName() +" "+ userToDebit.getLastName())
                            .accountNumber(userToDebit.getAccountNumber())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
        }
    }

    @Override
    public BankResponse transferFunds(TransferRequest request) {
        // Check if the source account exists
        boolean isSourceAccountExist = userRepository.existsByAccountNumber(request.getSourceAccountNumber());
        if (!isSourceAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        // Check if the destination account exists
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if (!isDestinationAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.DESTINATION_ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.DESTINATION_ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        // Retrieve the source and destination accounts
        User sourceAccount = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        User destinationAccount = userRepository.findByAccountNumber(request.getDestinationAccountNumber());

        // Check if the source account has sufficient balance for the transfer
        BigDecimal transferAmount = request.getAmount();
        BigDecimal sourceAccountBalance = sourceAccount.getAccountBalance();
        if (sourceAccountBalance.compareTo(transferAmount) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        // Perform the transfer
        sourceAccount.setAccountBalance(sourceAccountBalance.subtract(transferAmount));
        destinationAccount.setAccountBalance(destinationAccount.getAccountBalance().add(transferAmount));
        userRepository.save(sourceAccount);
        userRepository.save(destinationAccount);

        //Send Email Alert
        EmailDetails debitAlert = EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipient(sourceAccount.getEmail())
                .messageBody("The sum of "+request.getAmount()+" has been debited from your account!. Your current balance is: "+sourceAccount.getAccountBalance())
                .build();
        emailService.sendEmailAlert(debitAlert);

        //Save Transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType("CREDIT")
                .accountNumber(destinationAccount.getAccountNumber())
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto);

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(destinationAccount.getEmail())
                .messageBody("The sum of "+request.getAmount()+" has been credited to your account!. Your current balance is: "+destinationAccount.getAccountBalance())
                .build();
        emailService.sendEmailAlert(creditAlert);

        TransactionDto transactionDto2 = TransactionDto.builder()
                .transactionType("DEBIT")
                .accountNumber(sourceAccount.getAccountNumber())
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto2);

        // Build and return the response
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_TRANSFER_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_TRANSFER_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(sourceAccount.getFirstName() + " " + sourceAccount.getOtherName() + " " + sourceAccount.getLastName())
                        .accountNumber(sourceAccount.getAccountNumber())
                        .accountBalance(sourceAccount.getAccountBalance())
                        .build())
                .build();
    }

    private String generateReferenceCode() {
        return UUID.randomUUID().toString();
    }
}
