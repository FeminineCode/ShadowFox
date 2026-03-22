package com.bank;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {

    @Test
    public void testDeposit() {
        BankAccount account = new BankAccount(1000);
        account.deposit(500);
        assertEquals(1500, account.getBalance());
    }

    @Test
    public void testWithdraw() {
        BankAccount account = new BankAccount(1000);
        account.withdraw(300);
        assertEquals(700, account.getBalance());
    }

    @Test
    public void testInsufficientBalance() {
        BankAccount account = new BankAccount(500);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(600);
        });

        assertEquals("Insufficient balance.", exception.getMessage());
        assertEquals(500, account.getBalance());
    }

    @Test
    public void testNegativeDeposit() {
        BankAccount account = new BankAccount(1000);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            account.deposit(-200);
        });

        assertEquals("Deposit amount must be greater than zero.", exception.getMessage());
    }

    @Test
    public void testTransactionHistory() {
        BankAccount account = new BankAccount(1000);
        account.deposit(200);
        account.withdraw(100);

        List<String> history = account.getTransactionHistory();

        assertEquals(3, history.size());
        assertEquals("Account created with balance: 1000.0", history.get(0));
        assertEquals("Deposited: 200.0", history.get(1));
        assertEquals("Withdrew: 100.0", history.get(2));
    }
}