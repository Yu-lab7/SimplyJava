package Ex4.server;

import java.io.*;
import java.util.*;

public class PassAccount extends Ex2.Account {
    private String accountPassword;
    private String accountNumber;
    private List<String> transactionHistory = new ArrayList<>();

    public PassAccount(String bankName, String branchName, String accountHolder, int initialValue, String accountPassword, String accountNumber) {
        super(bankName, branchName, accountHolder, initialValue);
        this.accountPassword = accountPassword;
        this.accountNumber = accountNumber;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    public String getHistory() {
        return String.join("\n", transactionHistory);
    }

    public void transfer(PassAccount targetAccount, int amount) {
        if (amount > 0 && amount <= getBalance()) {
            int tmp = draw(amount);
            targetAccount.deposit(amount);
            addTransaction("Transferred " + amount + " to " + targetAccount.getAccountNumber());
            targetAccount.addTransaction("Received " + amount + " from " + getAccountNumber());
        } else {
            System.out.println("Invalid transfer amount.");
        }
    }
}