package app.javacode;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private final int accountNumber;
    private double balance;
    private final Lock lock = new ReentrantLock();

    public BankAccount(int accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public void deposit(double amount) {
        lock.lock();
        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be greater than 0");
            }

            balance += amount;
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(double amount) {
        lock.lock();
        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be greater than 0");
            }

            if (amount > balance) {
                throw new IllegalArgumentException("Amount must be less than balance");
            }

            balance -= amount;
        } finally {
            lock.unlock();
        }


    }

    public double getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public Lock getLock() {
        return lock;
    }
}
