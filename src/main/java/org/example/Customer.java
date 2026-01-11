package org.example;

public class Customer extends User {

    private  CustomerAccount account;
    private double balance;
    private String passwordHash;

    public Customer(String userId, String email, String name, String password) {
        super(userId, email, name);

        this.account = new CustomerAccount("ACC-"+userId);
        this.passwordHash = password;
    }

    public void topUp(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        balance += amount;
    }

    public double getBalance() {
        return balance;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public CustomerAccount getAccount() { return account; }
    public String getCustomerId() {
        return getUserId();
    }

    public void setAccount(CustomerAccount account) { this.account = account; }
}
