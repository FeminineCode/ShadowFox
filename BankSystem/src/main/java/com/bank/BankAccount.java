package com.bank;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {

    private double balance;
    private List<String> transactionHistory;

    // Constructor
    public BankAccount(double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        transactionHistory.add("Account created with balance: " + initialBalance);
    }

    // Deposit
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        balance = balance + amount;   // update balance
        transactionHistory.add("Deposited: " + amount);
    }

    // Withdraw (FIXED)
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }

        if (amount > balance) {
            transactionHistory.add("Failed withdrawal attempt: " + amount);
            throw new IllegalArgumentException("Insufficient balance.");
        }

        balance = balance - amount;   // ⭐ IMPORTANT FIX
        transactionHistory.add("Withdrew: " + amount);
    }

    // Get Balance
    public double getBalance() {
        return balance;
    }

    // Transaction History
    public List<String> getTransactionHistory() {
        return transactionHistory;
    }
}