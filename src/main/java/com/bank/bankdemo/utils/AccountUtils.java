package com.bank.bankdemo.utils;

import java.time.Year;
import java.util.Random;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account";
    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created!";
    public static final String ACCOUNT_DOES_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_DOES_NOT_EXIST_MESSAGE = "User with provided account not exist";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_SUCCESS = "User Account Found";
    public static final String ACCOUNT_CREDITED_SUCCESS = "005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "User Account Credited Successfully";
    public static final String INSUFFICIENT_BALANCE_CODE = "006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient Balance";
    public static final String ACCOUNT_DEBITED_SUCCESS = "007";
    public static final String ACCOUNT_DEBITED_MESSAGE = "Account has been debited successfully";
    public static final String DESTINATION_ACCOUNT_DOES_NOT_EXIST_CODE = "008";
    public static final String DESTINATION_ACCOUNT_DOES_NOT_EXIST_MESSAGE = "Destination Account is invalid";
    public static final String ACCOUNT_TRANSFER_SUCCESS = "009";
    public static final String ACCOUNT_TRANSFER_SUCCESS_MESSAGE = "Money Transferred successfully";

    public static String generateAccountNumber(){
        /**
         * 2023 + randomSixDigits
         */

        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        //generate a random number between min and max

        Random random = new Random();
        int randNumber = random.nextInt(max - min + 1) + min;
        //converts the currentYear and RandomNumber to string then concatenate

        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);
        StringBuilder accountNumber = new StringBuilder();

        return accountNumber.append(year).append(randomNumber).toString();
    }
}
