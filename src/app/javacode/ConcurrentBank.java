package app.javacode;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentBank {
    private final ConcurrentHashMap<Integer, BankAccount> accounts = new ConcurrentHashMap<>();
    private int accountsCount = 0;

    public BankAccount createAccount(double initialBalance) {
        int accountNumber = accountsCount++;
        BankAccount newAccount = new BankAccount(accountNumber, initialBalance);
        accounts.put(accountNumber, newAccount);
        return newAccount;
    }

    public void transfer(BankAccount from, BankAccount to, double amount) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Accounts must not be null");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        if (from == to) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        BankAccount firstLock = from.getAccountNumber() < to.getAccountNumber() ? from : to;
        BankAccount secondLock = from.getAccountNumber() < to.getAccountNumber() ? to : from;

        firstLock.getLock().lock();
        try {
            secondLock.getLock().lock();
            try{
                from.withdraw(amount);
                to.deposit(amount);
            } finally {
                secondLock.getLock().unlock();
            }
        } finally {
            firstLock.getLock().unlock();
        }
    }

    public double getTotalBalance() {
        return accounts.values().stream().mapToDouble(BankAccount::getBalance).sum();
    }
}
